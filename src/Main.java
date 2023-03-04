import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void output_field(char[][] matrix) {
        for (int i=0; i<3; i++) {
            for (int j = 0; j < 3; j++)
                if (matrix[i][j] != ' ')
                    System.out.print(matrix[i][j] + " ");
                else
                    System.out.print("- ");

            System.out.println();
        }
        System.out.println();
    }

    public static boolean Correct_input(String movestring) {
        boolean [] check = new boolean[4];

        check[0] = Character.isDigit(movestring.charAt(0));
        if (check[0]) {
            int x = Integer.parseInt(String.valueOf(movestring.charAt(0)));
            check[0] = (0<=x && x<=3);
        }

        check[1] = movestring.charAt(1)==',';
        check[2] = Character.isDigit(movestring.charAt(2));

        if (check[2]) {
            int y = Integer.parseInt(String.valueOf(movestring.charAt(2)));
            check[2]=(1<=y && y<=3);
        }

        check[3] = movestring.length()==3;

        boolean result = check[0]&&check[1]&&check[2]&&check[3];

        if (!result) System.out.println("Incorrect input.\n");

        return result;
    }
    public static boolean occupied(int i, int j, char[][] matrix) {
        //boolean result = matrix[i][j]!=Character.MIN_VALUE;
        boolean result = matrix[i][j]!=' ';

        if (result) System.out.println("The position is occupied.\n");

        return result;
    }
    public static String check_result_game(char[][] matrix, char player) {
        String result = "Nothing";

        boolean []wins = new boolean[8];

        wins[0] = matrix[0][0]==player &&
                matrix[1][0]==player &&
                matrix[2][0]==player;

        wins[1] = matrix[0][1]==player &&
                matrix[1][1]==player &&
                matrix[2][1]==player;

        wins[2] = matrix[0][2]==player &&
                matrix[1][2]==player &&
                matrix[2][2]==player;

        wins[3] = matrix[0][0]==player && matrix[0][1]==player && matrix[0][2]==player;
        wins[4] = matrix[1][0]==player && matrix[1][1]==player && matrix[1][2]==player;
        wins[5] = matrix[2][0]==player && matrix[2][1]==player && matrix[2][2]==player;

        wins[6] = matrix[0][0]==player && matrix[1][1]==player && matrix[2][2]==player;
        wins[7] = matrix[2][0]==player && matrix[1][1]==player && matrix[0][2]==player;

        boolean win = wins[0] || wins[1] || wins[2] || wins[3] || wins[4] || wins[5] || wins[6] || wins[7];

        if (win) result = "Win";
        else {
            boolean draw = true;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++)
                    if (matrix[i][j] == ' ') { //
                        draw = false;
                        break;
                    }
                if (!draw) break;
            }

            if (draw) result = "Draw";
        }

        return result;
    }
    public static boolean Player_game(char[][] matrix, char player) {
        int index_i = -1, index_j = -1;
        String movestring = "";

        while (true) {
            System.out.print("Player "+player+": ");
            Scanner sc = new Scanner(System.in);
            movestring = sc.nextLine().toUpperCase();

            if (Correct_input(movestring)) {
                index_i = Integer.parseInt(String.valueOf(movestring.charAt(0))) - 1;
                index_j = Integer.parseInt(String.valueOf(movestring.charAt(2))) - 1;
                if (!occupied(index_i, index_j, matrix)) {
                    matrix[index_i][index_j] = player;
                    break;
                }
            }

        }

        String result_game = check_result_game(matrix,player);

        boolean end_game = false;

        if (result_game.equals("Win")) {
            System.out.println("Player "+player+" win!");
            end_game = true;
        }
        if (result_game.equals("Draw")) {
            System.out.println("Draw");
            end_game = true;
        }

        return end_game;
    }

    /*
    public static boolean not_yet(int j, ArrayList<Integer> have_already_been) {
        for (Integer el: have_already_been)
            if (el == j) return false;

        return true;
    }


    public static ArrayList<Node> sort_by_depth(ArrayList<Node> nodes) {
        ArrayList<Node> result_nodes = new ArrayList<>();

        ArrayList<Integer> have_already_been = new ArrayList<>();
        for (int i=0; i<nodes.size(); i++) {
            int imin = 0, min = -1;

            for (int j=0; j<nodes.size(); j++)
                if (min > nodes.get(j).nearest_resulting_depth && not_yet(j,have_already_been)) {
                    min = nodes.get(j).nearest_resulting_depth;
                    imin = j;
                }

            result_nodes.add(nodes.get(imin));
            have_already_been.add(imin);
        }

        return result_nodes;
    }
    */

    public static Node get_max_by_score(ArrayList<Node> nodes) {
        int imax = 0;
        double max = -1;

        for (int j=0; j<nodes.size(); j++)
            if (max < nodes.get(j).score) {
                max = nodes.get(j).score;
                imax = j;
            }

        return nodes.get(imax);
    }

    public static int[] Intelligence(char[][] matrix, char player) {
        int [] result_indexes = {-1,-1};
        Node.player_bot = player;
        Node.count = 0;
        Node tree = new Node(matrix, player, 0);

        /*
        ArrayList<Node> win_nodes = new ArrayList<>();
        ArrayList<Node> draw_nodes = new ArrayList<>();
        ArrayList<Node> loss_nodes = new ArrayList<>();
        for (Node el: tree.nexts) {
            if (el.situation.equals("Win")) win_nodes.add(el);
            if (el.situation.equals("Draw")) draw_nodes.add(el);
            if (el.situation.equals("Loss")) loss_nodes.add(el);
        }

        Coordinate result;

        if (win_nodes.size()!=0) {
            Node minimum_by_depth_win_node = get_min_by_depth(win_nodes);
            result = minimum_by_depth_win_node.current_move;
        }
        else
        if (draw_nodes.size()!=0) {
            Node minimum_by_depth_draw_node = get_min_by_depth(draw_nodes);
            result = minimum_by_depth_draw_node.current_move;
        }
        else {
            Node minimum_by_depth_loss_node = get_min_by_depth(loss_nodes); //мб лучше чтобы бот дольше сопротивлялся при проигрывании?
            result = minimum_by_depth_loss_node.current_move;
        }

        result_indexes[0] = result.i;
        result_indexes[1] = result.j;
*/

        Node maximum_score_node = get_max_by_score(tree.nexts); //мб лучше чтобы бот дольше сопротивлялся при проигрывании?
        Coordinate result = maximum_score_node.current_move;

        result_indexes[0] = result.i;
        result_indexes[1] = result.j;


        return result_indexes;
    }

    /*
    public static char get_alternative_player(char player) {
        if (player == 'X')
            return 'O';
        else
            return 'X';
    }*/

    public static boolean Bot_game(char[][] matrix, char player) {
        //char opponent = get_alternative_player(player);
        int [] indexes = Intelligence(matrix, player);
        System.out.println("Player bot "+player+": "+(indexes[0]+1)+","+(indexes[1]+1));

        matrix[indexes[0]][indexes[1]] = player;

        String result_game = check_result_game(matrix,player);
        boolean end_game = false;

        if (result_game.equals("Win")) {
            System.out.println("You lose");
            end_game = true;
        }
        if (result_game.equals("Draw")) {
            System.out.println("Draw");
            end_game = true;
        }

        return end_game;
    }

    public static void Man_vs_Man() {
        char player1 = 'X', player2 = 'O';

        char [][] matrix = new char[3][3];
        boolean end_game;

        while (true) {
            end_game = Player_game(matrix,player1);
            if (end_game) break;

            end_game = Player_game(matrix,player2);
            if (end_game) break;
        }
    }
    public static void Man_vs_Bot() {
        char player1 = 'X', player2 = 'O';

        //char [][] matrix = new char[3][3];
        char [][] matrix = {{' ',' ',' '},
                            {' ',' ',' '},
                            {' ',' ',' '}};
        boolean end_game;

        while (true) {
            output_field(matrix);

            end_game = Player_game(matrix,player1);
            if (end_game) break;

            output_field(matrix);

            end_game = Bot_game(matrix,player2);
            if (end_game) break;
        }

        output_field(matrix);
    }

    public static void Bot_vs_Man() {
        char player1 = 'X', player2 = 'O';

        //char [][] matrix = new char[3][3];
        char [][] matrix = {{' ',' ',' '},
                {' ',' ',' '},
                {' ',' ',' '}};
        boolean end_game;

        while (true) {
            output_field(matrix);

            end_game = Bot_game(matrix,player1);
            if (end_game) break;

            output_field(matrix);

            end_game = Player_game(matrix,player2);
            if (end_game) break;
        }

        output_field(matrix);
    }


    public static void Test() {
        char [][] matrix = {{'X','O','X'},
                            {'X',' ',' '},
                            {' ','O',' '}};


                Intelligence(matrix, 'O');
    }

    public static void main(String[] args) {
        System.out.print("x or o : ");
        Scanner sc = new Scanner(System.in);

        if (sc.nextLine().charAt(0) == 'x')
            Man_vs_Bot();
        else
            Bot_vs_Man();

        // char a = ''; это Character.MIN_VALUE

        //Test();

    }
}
