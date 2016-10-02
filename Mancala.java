
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mancala {
	
	private int startPlayer;
	private int depth;
	private State result;

	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedWriter bw = null;
		try{
		//Date sd = new Date();
		//System.out.println(new Date());
		Mancala ass2 = new Mancala();
		String inputFile = args[1];
		File file = new File(inputFile);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		int gameType = Integer.parseInt(br.readLine().trim());
		File nextStateFile = new File("next_state.txt");
		if(!nextStateFile.exists()){
			nextStateFile.createNewFile();
		}
		FileWriter frw = new FileWriter(nextStateFile);
		bw = new BufferedWriter(frw);
		
		if(gameType==1){
			ass2.Greedy(br,bw);
		} else if(gameType==2){
			ass2.MiniMax(br,bw);
		} else if(gameType==3){
			ass2.AlphaBeta(br,bw);
		} else if(gameType==4){
			
		}
		//System.out.println(new Date());
		//System.out.println(new Date().getTime()-sd.getTime());
		} catch(Exception exception){
			System.out.println("Some Error !!! "+ exception.getMessage());
		} finally {
			bw.close();
		}
	}
	
	private void Greedy(BufferedReader br, BufferedWriter bw) throws NumberFormatException, IOException {
		try{
		int startPlayer = Integer.parseInt(br.readLine().trim());
		this.startPlayer = startPlayer;
		Integer.parseInt(br.readLine().trim());
		this.depth = 1;
		String[] boardStateForPlayer2 = br.readLine().trim().split(" ");
		String[] boardStateForPlayer1 = br.readLine().trim().split(" ");
		int manCalaPlayer2 = Integer.parseInt(br.readLine().trim());
		int manCalaPlayer1 = Integer.parseInt(br.readLine().trim());
		State state = InitailState(startPlayer, 1, boardStateForPlayer2, boardStateForPlayer1, manCalaPlayer2, manCalaPlayer1);
		minmax_max(state, 1, false, null);
		if(result==null){
			Display(state,bw);
		} else {
			Display(result,bw);
		}
		} catch(Exception exception){
			System.out.println("Some Error !!! "+ exception.getMessage());
		} finally {
			bw.close();
		}
	}

	private void MiniMax(BufferedReader br, BufferedWriter bw) throws NumberFormatException, IOException {
		BufferedWriter bw1 = null;
		try{
		int startPlayer = Integer.parseInt(br.readLine().trim());
		this.startPlayer = startPlayer;
		int depth = Integer.parseInt(br.readLine().trim());
		this.depth = depth;
		String[] boardStateForPlayer2 = br.readLine().trim().split(" ");
		String[] boardStateForPlayer1 = br.readLine().trim().split(" ");
		int manCalaPlayer2 = Integer.parseInt(br.readLine().trim());
		int manCalaPlayer1 = Integer.parseInt(br.readLine().trim());
		State state = InitailState(startPlayer, depth, boardStateForPlayer2, boardStateForPlayer1, manCalaPlayer2, manCalaPlayer1);
		bw1 = getTraceLogWriter();
		bw1.write("Node,Depth,Value");
		bw1.newLine();
		minmax_max(state,depth, false, bw1);
		bw1.write((state.getPlayerName()+","+ state.getDepth()+"," + state.getValue()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
		if(result==null){
			Display(state,bw);
		} else {
			Display(result,bw);
		}	
		} catch(Exception exception){
			System.out.println("Some Error !!! "+ exception.getMessage());
		} finally {
			bw1.close();
		}
		
	}

	private void AlphaBeta(BufferedReader br, BufferedWriter bw) throws NumberFormatException, IOException {
		BufferedWriter bw1 = null;
		try{
		int startPlayer = Integer.parseInt(br.readLine().trim());
		this.startPlayer = startPlayer;
		int depth = Integer.parseInt(br.readLine().trim());
		this.depth = depth;
		String[] boardStateForPlayer2 = br.readLine().trim().split(" ");
		String[] boardStateForPlayer1 = br.readLine().trim().split(" ");
		int manCalaPlayer2 = Integer.parseInt(br.readLine().trim());
		int manCalaPlayer1 = Integer.parseInt(br.readLine().trim());
		State state = InitailState(startPlayer, depth, boardStateForPlayer2, boardStateForPlayer1, manCalaPlayer2, manCalaPlayer1);
		state.setAlpha(Integer.MIN_VALUE);
		state.setBeta(Integer.MAX_VALUE);
		bw1 = getTraceLogWriter();
		bw1.write("Node,Depth,Value,Alpha,Beta");
		bw1.newLine();
		AlphaBeta_max(state,depth, false,bw1);
		bw1.write((state.getPlayerName()+","+ state.getDepth()+"," + state.getValue()+","+state.getAlpha()+","+state.getBeta()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
		if(result==null){
			Display(state,bw);
		} else {
			Display(result,bw);
		}
		
		} catch(Exception exception){
			System.out.println("Some Error !!! "+ exception.getMessage());
		} finally {
			bw1.close();
		}
	}

	private BufferedWriter getTraceLogWriter() throws IOException {
		BufferedWriter bw;
		File traverseLog = new File("traverse_log.txt");
		if(!traverseLog.exists()){
			traverseLog.createNewFile();
		}
		FileWriter frw = new FileWriter(traverseLog);
		bw = new BufferedWriter(frw);
		return bw;
	}

	private int AlphaBeta_max(State state2, int depth, boolean father, BufferedWriter bw1) throws IOException {
		if(TerminalTest(state2.getStateOfGame(), depth, startPlayer)){
			if(depth!=0){
				bw1.write((state2.getPlayerName()+","+ state2.getDepth()+"," + state2.getValue()+","+state2.getAlpha()+","+state2.getBeta()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
				bw1.newLine();
			}
			return Evaluation(state2.getStateOfGame(), startPlayer);
		} 
		int value = Integer.MIN_VALUE;
		List<State> stateList = Successor(state2.getStateOfGame(),startPlayer);
		for(State state : stateList){
			state.setValue(Integer.MAX_VALUE);
			state.setAlpha(state2.getAlpha());
			state.setBeta(state2.getBeta());
			state.setDepth((this.depth-depth)+1);
			state.setPlayerName(MakePlayerName(state, startPlayer));
			bw1.write((state2.getPlayerName()+","+ state2.getDepth()+"," + state2.getValue()+","+state2.getAlpha()+","+state2.getBeta()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
			bw1.newLine();
			if(state.isLast()){
				state.setValue(Integer.MIN_VALUE);
				int midValue = AlphaBeta_max(state,depth, true, bw1);
				value = Math.max(value, midValue);
				state.setValue(midValue);
				bw1.write((state.getPlayerName()+","+ state.getDepth()+"," + state.getValue()+","+state.getAlpha()+","+state.getBeta()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
				bw1.newLine();
				bw1.flush();
				if(father){
					state2.setValue(value);
					if(value >= state2.getBeta()){
						return value;
					} else if(value>state2.getAlpha()){
						state2.setAlpha(value);
					}
				} else {
					state2.setValue(Math.max(state2.getValue(), value));
					if(value >= state2.getBeta()){
						return value;
					} else {
						state2.setAlpha(Math.max(value, state2.getAlpha()));
					}
				}
				if(state.depth==1&&GameFinishes(state.stateOfGame, startPlayer)){
					if(result==null) {
						result = state;
					} else {
						if(result.getValue() < state.getValue()){
							result = state;
						}
					}
				}
				
			} else {
				int midValue = AlphaBeta_min(state,depth-1, false, bw1);
				value = Math.max(value, midValue);
				state.setValue(midValue);
				bw1.write((state.getPlayerName()+","+ state.getDepth()+"," + state.getValue()+","+state.getAlpha()+","+state.getBeta()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
				bw1.newLine();
				if(state.depth==1&&!state.isLast()){
					if(result==null) {
						result = state;
					} else {
						if(result.getValue() < state.getValue()){
							result = state;
						}
					}
				}
				if(father){
					state2.setValue(value);
					if(value >= state2.getBeta()){
						return value;
					} else if(value>state2.getAlpha()){
						state2.setAlpha(value);
					} 
				} else {
					state2.setValue(Math.max(state2.getValue(), value));
					if(value >= state2.getBeta()){
						return value;
					} else {
						state2.setAlpha(Math.max(value, state2.getAlpha()));
					}
				}
			}
		}
		return value;
	}
	
	private int AlphaBeta_min(State state2, int depth, boolean father, BufferedWriter bw1) throws IOException {
		int startPlayer = 0;
		if(this.startPlayer ==2){
			startPlayer = 1;
		}else{
			startPlayer=2;
		}
			
		if(TerminalTest(state2.getStateOfGame(), depth, startPlayer)){
			if(depth!=0){
				bw1.write((state2.getPlayerName()+","+ state2.getDepth()+"," + state2.getValue()+","+state2.getAlpha()+","+state2.getBeta()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
				bw1.newLine();
			}
			return Evaluation(state2.getStateOfGame(),startPlayer);
		} 
		int value = Integer.MAX_VALUE;
		List<State> stateList = Successor(state2.getStateOfGame(),startPlayer);
		for(State state : stateList){
			state.setValue(Integer.MIN_VALUE);
			state.setAlpha(state2.getAlpha());
			state.setBeta(state2.getBeta());
			state.setDepth((this.depth-depth)+1);
			state.setPlayerName(MakePlayerName(state, startPlayer));
			bw1.write((state2.getPlayerName()+","+ state2.getDepth()+"," + state2.getValue()+","+state2.getAlpha()+","+state2.getBeta()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
			bw1.newLine();
			if(state.isLast()){
				state.setValue(Integer.MAX_VALUE);
				int midValue = AlphaBeta_min(state,depth, true, bw1);
				value = Math.min(value,midValue);
				state.setValue(midValue);
				bw1.write((state.getPlayerName()+","+ state.getDepth()+"," + state.getValue()+","+state.getAlpha()+","+state.getBeta()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
				bw1.newLine();
				if(father){
					state2.setValue(value);
					if(value <= state2.getAlpha()){
						return value;
					} else if(value<state2.getBeta()){
						state2.setBeta(value);
					}
				} else {
					state2.setValue(Math.min(state2.getValue(), value));
					if(value <= state2.getAlpha()){
						return value;
					} else {
						state2.setBeta(Math.min(value, state2.getBeta()));
					}
				}
				
			} else {
				int midValue = AlphaBeta_max(state,depth-1, false, bw1);
				value = Math.min(value, midValue);
				state.setValue(midValue);
				bw1.write((state.getPlayerName()+","+ state.getDepth()+"," + state.getValue()+","+state.getAlpha()+","+state.getBeta()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
				bw1.newLine();
				if(father){
					state2.setValue(value);
					if(value <= state2.getAlpha()){
						return value;
					} else if(value<state2.getBeta()){
						state2.setBeta(value);
					} 
				} else {
					state2.setValue(Math.min(state2.getValue(), value));
					if(value <= state2.getAlpha()){
						return value;
					} else {
						state2.setBeta(Math.min(value, state2.getBeta()));
					}
				}	
			}
		}
		return value;
	}

	private State InitailState(int startPlayer, int depth, String[] boardStateForPlayer2, String[] boardStateForPlayer1,
			int manCalaPlayer2, int manCalaPlayer1) throws NumberFormatException {
		int[][] stateOfGame = new int[2][boardStateForPlayer2.length+1];
		stateOfGame[1][0] = manCalaPlayer1;
		int count=1;
		for(String i : boardStateForPlayer1){
			stateOfGame[1][count] = Integer.parseInt(i);
			count++;
		}
		stateOfGame[0][0] = manCalaPlayer2;
		count=1;
		for(String i : boardStateForPlayer2){
			stateOfGame[0][count] = Integer.parseInt(i);
			count++;
		}
		State state = new State();
		state.setStateOfGame(stateOfGame);
		state.setLast(false);
		state.setAction("root");
		state.setValue(Integer.MIN_VALUE);
		state.setDepth(this.depth-depth);
		state.setPlayerName(MakePlayerName(state,startPlayer));
		return state;
	}

	private void Display(State result2, BufferedWriter bw) throws IOException {
		for(int i =1;i<result2.getStateOfGame()[0].length;i++){
			bw.write(result2.getStateOfGame()[0][i]+ " ");
		}
		bw.newLine();
		for(int i =1;i<result2.getStateOfGame()[0].length;i++){
			bw.write(result2.getStateOfGame()[1][i]+ " ");
		}
		bw.newLine();
		bw.write(result2.getStateOfGame()[0][0]+"");
		bw.newLine();
		bw.write(result2.getStateOfGame()[1][0]+"");
		
	}

	private String MakePlayerName(State state, int startPlayerValue) {
		if(state.getAction().equals("root")){
			return "root";
		} else {
			if(startPlayerValue==2){
				return "A"+(Integer.parseInt(state.getAction())+1);
			} else {
				return "B"+(Integer.parseInt(state.getAction())+1);
			}
		}
	}

	private int minmax_max(State state2, int depth, boolean father, BufferedWriter bw1) throws IOException {
		if(TerminalTest(state2.getStateOfGame(), depth, startPlayer)){
			if(bw1!=null&&depth!=0){
				bw1.write((state2.getPlayerName()+","+ state2.getDepth()+"," + state2.getValue()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
				bw1.newLine();
			}
			return Evaluation(state2.getStateOfGame(), startPlayer);
		} 
		int value = Integer.MIN_VALUE;
		List<State> stateList = Successor(state2.getStateOfGame(),startPlayer);
		for(State state : stateList){
			state.setValue(Integer.MAX_VALUE);
			state.setDepth((this.depth-depth)+1);
			state.setPlayerName(MakePlayerName(state, startPlayer));
			if(bw1!=null){
				bw1.write((state2.getPlayerName()+","+ state2.getDepth()+"," + state2.getValue()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
				bw1.newLine();
			}
			if(state.isLast()){
				state.setValue(Integer.MIN_VALUE);
				int midValue = minmax_max(state,depth, true, bw1);
				value = Math.max(value, midValue);
				if(father){
					state2.setValue(value);
				} else {
					state2.setValue(Math.max(state2.getValue(), value));
				}
				state.setValue(midValue);
				if(bw1!=null){
					bw1.write((state.getPlayerName()+","+ state.getDepth()+"," + state.getValue()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
					bw1.newLine();
					bw1.flush();
				}
				if(state.depth==1&&GameFinishes(state.stateOfGame, startPlayer)){
					if(result==null) {
						result = state;
					} else {
						if(result.getValue() < state.getValue()){
							result = state;
						}
					}
				}
			} else {
				int midValue = minmax_min(state,depth-1, false, bw1);
				value = Math.max(value, midValue);
				if(father){
					state2.setValue(value);
				} else {
					state2.setValue(Math.max(state2.getValue(), value));
				}
				state.setValue(midValue);
				if(state.depth==1&&!state.isLast()){
					if(result==null) {
						result = state;
					} else {
						if(result.getValue() < state.getValue()){
							result = state;
						}
					}
				}
				if(bw1!=null){
					bw1.write((state.getPlayerName()+","+ state.getDepth()+"," + state.getValue()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
					bw1.newLine();
				}
			}
		}
		return value;
		
	}
	
	private int Evaluation(int[][] stateOfGame, int startPlayer2) {
		if(startPlayer == 1){
			if(GameFinished(stateOfGame, startPlayer2)){
				if(startPlayer2 == 2){
					int sum = 0;
					for(int i=1;i<stateOfGame[0].length;i++){
						sum = sum + stateOfGame[1][i];
					}
					return (stateOfGame[1][0]+sum) - stateOfGame[0][0];
				} else {
					int sum = 0;
					for(int i=1;i<stateOfGame[0].length;i++){
						sum = sum + stateOfGame[0][i];
					}
					return stateOfGame[1][0] - (stateOfGame[0][0]+sum);
				}
			}
			return stateOfGame[1][0] - stateOfGame[0][0]; 
		} else {
			if(GameFinished(stateOfGame, startPlayer2)){
				if(startPlayer2 == 2){
					int sum = 0;
					for(int i=1;i<stateOfGame[0].length;i++){
						sum = sum + stateOfGame[1][i];
					}
					return stateOfGame[0][0] - (stateOfGame[1][0]+sum);
				} else {
					int sum = 0;
					for(int i=1;i<stateOfGame[0].length;i++){
						sum = sum + stateOfGame[0][i];
					}
					return (stateOfGame[0][0]+sum) - stateOfGame[1][0];
				}
			}
			return stateOfGame[0][0] - stateOfGame[1][0];
		}
	}

	private boolean TerminalTest(int[][] stateOfGame, int depth, int startPlayer) {
		if(depth == 0 || GameFinishes(stateOfGame, startPlayer)){
			return true;
		}
		return false;
	}

	private boolean GameFinished(int[][] stateOfGame, int startPlayer) {
		boolean flag = false;	
		for(int i = 1 ;i <stateOfGame[1].length; i++){
			if(stateOfGame[1][i] > 0){
				flag = true;
				break;
			}
		}
		if(flag == false){
			return true;
		} else {
			for(int i = 1 ;i <stateOfGame[0].length; i++){
				if(stateOfGame[0][i] > 0){
					return false;
				}
			}
		}
		return true;
	}
	
	
	private boolean GameFinishes(int[][] stateOfGame, int startPlayer) {
		boolean flag = false;
		for(int i = 1 ;i <stateOfGame[1].length; i++){
			if(stateOfGame[1][i] > 0){
				flag = true;
				break;
			}
		} 
		
		if(flag == true){
			return false;
		} else {
			for(int i = 1 ;i <stateOfGame[0].length; i++){
				if(stateOfGame[0][i] > 0){
					return false;
				}
			}
		}
		return true;
	}
	

	private int minmax_min(State state2, int depth, boolean father, BufferedWriter bw1) throws IOException {
		int startPlayer = 0;
		if(this.startPlayer ==2){
			startPlayer = 1;
		}else{
			startPlayer=2;
		}
			
		if(TerminalTest(state2.getStateOfGame(), depth, startPlayer)){
			if(bw1!=null&&depth!=0){
				bw1.write((state2.getPlayerName()+","+ state2.getDepth()+"," + state2.getValue()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
				bw1.newLine();
			}
			return Evaluation(state2.getStateOfGame(),startPlayer);
		} 
		int value = Integer.MAX_VALUE;
		List<State> stateList = Successor(state2.getStateOfGame(),startPlayer);
		for(State state : stateList){
			state.setValue(Integer.MIN_VALUE);
			state.setDepth((this.depth-depth)+1);
			state.setPlayerName(MakePlayerName(state, startPlayer));
			if(bw1!=null){
				bw1.write((state2.getPlayerName()+","+ state2.getDepth()+"," + state2.getValue()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
				bw1.newLine();
			}
			if(state.isLast()){
				state.setValue(Integer.MAX_VALUE);
				int midValue = minmax_min(state,depth, true, bw1);
				value = Math.min(value,midValue);
				if(father){
					state2.setValue(value);
				} else {
					state2.setValue(Math.min(state2.getValue(), value));
				}
				state.setValue(midValue);
				if(bw1!=null){
					bw1.write((state.getPlayerName()+","+ state.getDepth()+"," + state.getValue()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
					bw1.newLine();
				}
			} else {
				int midValue = minmax_max(state,depth-1, false, bw1);
				value = Math.min(value, midValue);
				if(father){
					state2.setValue(value);
				} else {
					state2.setValue(Math.min(state2.getValue(), value));
				}
				state.setValue(midValue);
				if(bw1!=null){
					bw1.write((state.getPlayerName()+","+ state.getDepth()+"," + state.getValue()).replaceAll(Integer.MAX_VALUE+"", "Infinity").replaceAll(Integer.MIN_VALUE+"", "-Infinity"));
					bw1.newLine();
				}
			}
		}
		return value;
	}

	
	private List<State> Successor(int[][] stateOfGame, int startPlayer2) {
		List<State> possibleStates = new ArrayList<State>();
		for(int i = 1 ;i <stateOfGame[0].length; i++){
			if(startPlayer2==2){
				if(stateOfGame[0][i]==0){
					continue;
				}
			} else {
				if(stateOfGame[1][i]==0){
					continue;
				}
			}
			State state = NextState(stateOfGame, i , startPlayer2);
			if(GameFinished(state.getStateOfGame(), 1)){
				state = FinishedState(state);
			}
			possibleStates.add(state);
			
		}
		return possibleStates;
	}

	private State FinishedState(State state) {
		int[][] stateOfGame = state.getStateOfGame();
		int sum = 0;
		for(int i=1;i<stateOfGame[0].length;i++){
			sum = sum + stateOfGame[1][i];
			stateOfGame[1][i] = 0;
		}
		stateOfGame[1][0] = stateOfGame[1][0]+sum;
		sum = 0;
		for(int i=1;i<stateOfGame[0].length;i++){
			sum = sum + stateOfGame[0][i];
			stateOfGame[0][i] = 0;
		}
		stateOfGame[0][0] = stateOfGame[0][0]+sum;
		state.setStateOfGame(stateOfGame);
		return state;
	}

	private State NextState(int[][] stateOfGame2, int i, int startPlayer2) {
		State state = new State();
		boolean falg = false, capture = false, specialCase = false;
		int capturedIndex = -1;
		int[][] stateOfGame = new int[stateOfGame2.length][stateOfGame2[0].length];
		if(startPlayer2==2){
			int valueToBeAddedToEachBox = stateOfGame2[0][i]/((stateOfGame2[0].length*2)-1);
			int valueToBeAddedToAdjacentBox = stateOfGame2[0][i]%((stateOfGame2[0].length*2)-1);
			if(valueToBeAddedToAdjacentBox==0 && valueToBeAddedToEachBox==1){
				specialCase = true;
			}
			stateOfGame[0][i] = 0;
			for(int j=1;j<=stateOfGame2[0].length*2;j++){
				if(i-j >= 1){
					if(valueToBeAddedToAdjacentBox > 0){
						stateOfGame[0][i-j] = stateOfGame2[0][i-j] + valueToBeAddedToEachBox + 1;
						valueToBeAddedToAdjacentBox--;
						if(stateOfGame2[0][i-j] == 0 && valueToBeAddedToEachBox < 2){
							if(stateOfGame[0][i-j] == 1 && valueToBeAddedToAdjacentBox==0 && capture==false){
								capture = true;
								capturedIndex = i-j;
							}
						}
					} else {
						stateOfGame[0][i-j] = stateOfGame2[0][i-j] + valueToBeAddedToEachBox;
					}
					
				} else if(i-j == 0){
					if(valueToBeAddedToAdjacentBox > 0){
						stateOfGame[0][0] = stateOfGame2[0][0] + valueToBeAddedToEachBox + 1;
						valueToBeAddedToAdjacentBox--;
						if(valueToBeAddedToAdjacentBox ==0){
							falg = true;
						}
					} else {
						stateOfGame[0][0] = stateOfGame2[0][0] + valueToBeAddedToEachBox;
					}
				} else if(j-i > 0 &&j-i < stateOfGame2[0].length){
					if(valueToBeAddedToAdjacentBox > 0){
						stateOfGame[1][j-i] = stateOfGame2[1][j-i] + valueToBeAddedToEachBox + 1;
						valueToBeAddedToAdjacentBox--;
					} else {
						stateOfGame[1][j-i] = stateOfGame2[1][j-i] + valueToBeAddedToEachBox;
					}
				} else if(j-i == stateOfGame2[0].length){
						stateOfGame[1][0] = stateOfGame2[1][0];
				}
			}
			boolean left = false;
			if(valueToBeAddedToAdjacentBox>0){
				left = true;
			}			
			for(int k = stateOfGame[0].length-1;k>=i;k--){
				if(valueToBeAddedToAdjacentBox > 0){
					if(k!=i){
						stateOfGame[0][k] = stateOfGame2[0][k] + valueToBeAddedToEachBox + 1;
						valueToBeAddedToAdjacentBox--;
					} else {
						stateOfGame[0][k] = valueToBeAddedToEachBox + 1;
						valueToBeAddedToAdjacentBox--;
					}
				} else {
					if(k!=i){
						stateOfGame[0][k] = stateOfGame2[0][k] + valueToBeAddedToEachBox;
					} else {
						stateOfGame[0][k] = valueToBeAddedToEachBox;
					}
				}
				if(left){
						if(stateOfGame[0][k] == 1  && valueToBeAddedToAdjacentBox==0 && capture==false){
							capture = true;
							capturedIndex = k;
						}
				} 
				if(valueToBeAddedToAdjacentBox==0){
					left = false;
				}
			}
			if(capture){
				stateOfGame[0][0] += stateOfGame[0][capturedIndex] + stateOfGame[1][capturedIndex];
				stateOfGame[0][capturedIndex] = 0;
				stateOfGame[1][capturedIndex] = 0;
			}
			if(specialCase){
				stateOfGame[0][0] += stateOfGame[0][i] + stateOfGame[1][i];
				stateOfGame[0][i] = 0;
				stateOfGame[1][i] = 0;
			}		
		} else {
			int valueToBeAddedToEachBox = stateOfGame2[1][i]/((stateOfGame2[0].length*2)-1);
			int valueToBeAddedToAdjacentBox = stateOfGame2[1][i]%((stateOfGame2[0].length*2)-1);
			if(valueToBeAddedToAdjacentBox==0 && valueToBeAddedToEachBox==1){
				specialCase = true;
			}
			stateOfGame[1][i] = 0;
			for(int j=1;j<=stateOfGame2[0].length*2;j++){
				if(i+j < stateOfGame2[0].length){
					if(valueToBeAddedToAdjacentBox > 0){
						stateOfGame[1][i+j] = stateOfGame2[1][i+j] + valueToBeAddedToEachBox + 1;
						valueToBeAddedToAdjacentBox--;
						if(stateOfGame2[1][i+j] == 0 && valueToBeAddedToEachBox == 0 && capture==false){
							if(valueToBeAddedToAdjacentBox==0){
								capture = true;
								capturedIndex = i+j;
							}
						}
					} else {
						stateOfGame[1][i+j] = stateOfGame2[1][i+j] + valueToBeAddedToEachBox;
					}
				} else if(i+j == (stateOfGame2[0].length)){
					if(valueToBeAddedToAdjacentBox > 0){
						stateOfGame[1][0] = stateOfGame2[1][0] + valueToBeAddedToEachBox + 1;
						valueToBeAddedToAdjacentBox--;
						if(valueToBeAddedToAdjacentBox ==0){
							falg = true;
						}
					} else {
						stateOfGame[1][0] = stateOfGame2[1][0] + valueToBeAddedToEachBox;
					}
				} else if((2*stateOfGame2[0].length)-(i+j) > 0 && (2*stateOfGame2[0].length)-(i+j) < (stateOfGame2[0].length)){
					if(valueToBeAddedToAdjacentBox > 0){
						stateOfGame[0][(2*stateOfGame2[0].length)-(i+j)] = stateOfGame2[0][(2*stateOfGame2[0].length)-(i+j)] + valueToBeAddedToEachBox + 1;
						valueToBeAddedToAdjacentBox--;
					} else {
						stateOfGame[0][(2*stateOfGame2[0].length)-(i+j)] = stateOfGame2[0][(2*stateOfGame2[0].length)-(i+j)] + valueToBeAddedToEachBox;
					}
				} else if(i+j == (2*stateOfGame2[0].length)){
						stateOfGame[0][0] = stateOfGame2[0][0];
				}
			}
			
			boolean left = false;
			if(valueToBeAddedToAdjacentBox>0){
				left = true;
			}
			
			for(int k = 1;k<=i;k++){
				if(valueToBeAddedToAdjacentBox > 0){
					if(k != i){
						stateOfGame[1][k] = stateOfGame2[1][k] + valueToBeAddedToEachBox + 1;
						valueToBeAddedToAdjacentBox--;
					} else {
						stateOfGame[1][k] = valueToBeAddedToEachBox + 1;
						valueToBeAddedToAdjacentBox--;
					}
					
				} else {
					if(k != i){
						stateOfGame[1][k] = stateOfGame2[1][k] + valueToBeAddedToEachBox;
					} else {
						stateOfGame[1][k] = valueToBeAddedToEachBox;
					}
				}
				if(left){
					if(stateOfGame[1][k] == 1  && valueToBeAddedToAdjacentBox==0 && capture==false){
						capture = true;
						capturedIndex = k;
					}
				
				}
				if(valueToBeAddedToAdjacentBox==0){
					left = false;
				}
			}
			
			if(capture){
				stateOfGame[1][0] += stateOfGame[1][capturedIndex] + stateOfGame[0][capturedIndex];
				stateOfGame[0][capturedIndex] = 0;
				stateOfGame[1][capturedIndex] = 0;
			}
			
			if(specialCase){
				stateOfGame[1][0] += stateOfGame[1][i] + stateOfGame[0][i];
				stateOfGame[0][i] = 0;
				stateOfGame[1][i] = 0;
			}
	
		}
		state.setStateOfGame(stateOfGame);
		state.setAction(i+"");
		state.setLast(falg);
		return state;
	}

	private class State {
		private int[][] stateOfGame;
		private String action;
		private boolean isLast;
		private int value;
		private String playerName;
		private int alpha;
		private int beta;
		private int depth;

		public int getAlpha() {
			return alpha;
		}
		public void setAlpha(int alpha) {
			this.alpha = alpha;
		}
		public int getBeta() {
			return beta;
		}
		public void setBeta(int beta) {
			this.beta = beta;
		}
		public int getDepth() {
			return depth;
		}
		public void setDepth(int depth) {
			this.depth = depth;
		}
		public String getPlayerName() {
			return playerName;
		}
		public void setPlayerName(String playerName) {
			this.playerName = playerName;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public int[][] getStateOfGame() {
			return stateOfGame;
		}
		public void setStateOfGame(int[][] stateOfGame) {
			this.stateOfGame = stateOfGame;
		}
		public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
		}
		public boolean isLast() {
			return isLast;
		}
		public void setLast(boolean isLast) {
			this.isLast = isLast;
		}	
	}
}
