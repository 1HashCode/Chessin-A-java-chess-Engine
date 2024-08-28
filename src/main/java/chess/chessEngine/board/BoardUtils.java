package chess.chessEngine.board;

import java.util.HashMap;
import java.util.Map;

public class BoardUtils {
    public static final boolean[]FIRST_COLUMN = initColumn(0);
    public static final boolean[]SECOND_COLUMN =initColumn(1);
    public static final boolean[]SEVENTH_COLUMN =initColumn(6);
    public static final boolean[]EIGHT_COLUMN =initColumn(7);

    public static final boolean[] EIGHT_RANK =initRow(0);
    public static final boolean[] SEVENTH_RANK =initRow(8);
    public static final boolean[] SIXTH_RANK =initRow(16);
    public static final boolean[] FIFTH_RANK =initRow(24);
    public static final boolean[] FOURTH_RANK =initRow(32);
    public static final boolean[] THIRD_RANK =initRow(40);

    public static final boolean[] SECOND_RANK =initRow(48);
    public static final boolean[] FIRST_RANK =initRow(56);
    private static final String[] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();

    private static String[] initializeAlgebraicNotation() {
        String[] finalArray = new String[64];
        int[] numbers = {8, 7, 6, 5, 4, 3, 2, 1};
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        int count = 0;
        for(int x : numbers) {
            for (String y : letters) {
                finalArray[count] = y + x;
                count++;
            }
        }
        return finalArray;
    }
    public static final Map<String,Integer>POSITION_TO_COORDINATE=initializePositionToCoordinateMap();


    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String,Integer>positionToCoordinate=new HashMap<>();
        for(int i=0;i<64;++i){
            positionToCoordinate.put(ALGEBRAIC_NOTATION[i],i );
        }
        return positionToCoordinate;
    }

    private static boolean[] initColumn(int i) {
        boolean[]column=new boolean[64];
        while(i<64){
            column[i]=true;
            i+=8;
        }
        return column;
    }

    private static boolean[] initRow(int i) {
        boolean[]row=new boolean[64];
        do{
            row[i]=true;
            i++;
        }
        while(i%8!=0);
        return row;
    }

    public static boolean isValidTile(int coordinate){
        return coordinate>=0 && coordinate<64;
    }

    public static int getCoordianteAtPosition(final String position){
        return POSITION_TO_COORDINATE.get(position);
    }

    public static String getPositionAtCoordinate(final int coordinate){
        return ALGEBRAIC_NOTATION[coordinate];
    }
}
