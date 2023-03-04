import java.util.ArrayList;

public class Node {

    public static char player_bot;

    public static int count;

    public Node prev;
    public ArrayList<Node> nexts;
    public int depth;

    public String situation; //Win, Draw, Nothing, Loss
    public double score;
    public char[][] matrix;
    public char player_now;

    public int nearest_resulting_depth;
    public Coordinate current_move;
    public ArrayList<Coordinate> free_coordinates;


    void copy_matrix(char[][] matrix) {
        this.matrix = new char[3][3];
        for (int i=0; i<3; i++)
            for (int j=0; j<3; j++)
                this.matrix[i][j] = matrix[i][j];
    }
    void calculate_free_coordinates() {
        free_coordinates = new ArrayList<>();
        for (int i=0; i<3; i++)
            for (int j=0; j<3; j++)
                if (matrix[i][j] == ' ')
                    free_coordinates.add(new Coordinate(i, j));
    }
    char[][] take_next_matrix(Coordinate coord) {
        char [][] matrix = new char[3][3];

        for (int i=0; i<3; i++)
            for (int j=0; j<3; j++)
                matrix[i][j] = this.matrix[i][j];

        matrix[coord.i][coord.j] = player_now;

        return matrix;
    }
    char get_alternative_player(char player) {
        if (player == 'X')
            return 'O';
        else
            return 'X';
    }
    void calculate_score() {
        switch (situation) {
        case "Nothing": score = 0; break;
        case "Win": score = 2; break;
        case "Draw": score = 1; break;
        case "Loss": score = -1; break;
    }
    }

    void calculate_nexts(){
         nexts = new ArrayList<>();
        for (Coordinate coord : free_coordinates) {
            Node nextNode = new Node(take_next_matrix(coord), get_alternative_player(player_now), depth +1);
            nextNode.prev = this;
            nextNode.current_move = coord;

            nexts.add(nextNode);

            /*
            if (player_now != player_bot && nextNode.situation.equals("Loss")) {
                this.situation = "Loss";
                break;
            }
            if (player_now == player_bot && nextNode.situation.equals("Win")) {
                this.situation = "Win";
                break;
            }*/
        }
/*
        if (!(this.situation.equals("Loss") || this.situation.equals("Win")))
            for (Node nextNode : nexts)
                if (nextNode.situation.equals("Draw")) {
                    this.situation = "Draw";
                    break;
                }*/

        //calculate_score();

        if (depth != 0 && this.situation.equals("Nothing")) {
            double sum = 0;
            for (Node nextNode : nexts)
                sum += nextNode.score;

            this.score = (sum / nexts.size()) / depth;
        }
    }

    public String check_result_game(char player) {
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

    public void calculate_situation() {
        if (depth == 0) {
            situation = "Nothing";
            return;
        }

        String result_game_bot = check_result_game(player_bot);
        String result_game_player = check_result_game(get_alternative_player(player_bot));

        if (result_game_bot.equals(result_game_player) && result_game_bot.equals("Nothing"))
            situation = "Nothing";
        else
            if (result_game_player.equals("Win"))
                situation = "Loss";
            else
                situation = result_game_bot; // Win or Draw

        if (!situation.equals("Nothing")) nearest_resulting_depth = depth;
        /*
        switch (situation) {
            case "Draw": nearest_resulting_depth *= 2;
            case "Loss": nearest_resulting_depth *= 3;
        }*/

        calculate_score();
    }

    public Node(char[][] matrix, char player_now, int depth) {
        copy_matrix(matrix);
        this.player_now = player_now;
        this.depth = depth;
        this.nearest_resulting_depth = 10; //так как ищем минимальное количество ходов до результата (победы, ничьи или проигрыша) (поэтому берем большее, а не 0)
        //System.out.println(depth);

        calculate_situation();

        if (situation.equals("Nothing")) { // && deep<100 даже не нужно, ибо просчитывает спокойно полностью
            calculate_free_coordinates();
            calculate_nexts();
        }

        //System.out.println(count);
        count++;

    }

}
