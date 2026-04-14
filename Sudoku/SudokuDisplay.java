/**
 * Gère l'affichage de la grille de Sudoku en mode console.
 *
 * Utilise les caractères Unicode de dessin de boîte (box-drawing characters)
 * pour produire un affichage structuré et lisible.
 *
 * Exemple d'affichage produit :
 *
 *   ╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗
 *   ║ 5 │ 3 │   ║   │ 7 │   ║   │   │   ║
 *   ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
 *   ║ 6 │   │   ║ 1 │ 9 │ 5 ║   │   │   ║
 *   ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
 *   ║   │ 9 │ 8 ║   │   │   ║   │ 6 │   ║
 *   ╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
 *   ...
 *   ╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝
 *
 * @author Groupe 13
 */
public class SudokuDisplay {

    // =========================================================================
    //  CARACTÈRES DE DESSIN DE BOÎTE
    // =========================================================================

    // -- Coins de la bordure extérieure --
    private static final String TOP_LEFT      = "╔";
    private static final String TOP_RIGHT     = "╗";
    private static final String BOTTOM_LEFT   = "╚";
    private static final String BOTTOM_RIGHT  = "╝";

    // -- Segments horizontaux --
    private static final String H_THICK       = "═══"; // Bordure externe et séparateur de blocs
    private static final String H_THIN        = "───"; // Séparateur interne entre cellules

    // -- Segments verticaux --
    private static final String V_THICK       = "║";   // Bordure externe et séparateur de blocs
    private static final String V_THIN        = "│";   // Séparateur interne entre cellules

    // -- Jonctions de la bordure supérieure --
    private static final String TOP_T_THICK   = "╦";   // Jonction épaisse (entre blocs)
    private static final String TOP_T_THIN    = "╤";   // Jonction fine (entre cellules)

    // -- Jonctions de la bordure inférieure --
    private static final String BOT_T_THICK   = "╩";   // Jonction épaisse (entre blocs)
    private static final String BOT_T_THIN    = "╧";   // Jonction fine (entre cellules)

    // -- Jonctions des séparateurs épais (entre blocs 3x3) --
    private static final String LEFT_THICK    = "╠";   // Bord gauche
    private static final String RIGHT_THICK   = "╣";   // Bord droit
    private static final String CROSS_THICK   = "╬";   // Croisement épais-épais
    private static final String CROSS_T_THICK = "╪";   // Croisement épais-fin

    // -- Jonctions des séparateurs fins (entre cellules d'un même bloc) --
    private static final String LEFT_THIN     = "╟";   // Bord gauche
    private static final String RIGHT_THIN    = "╢";   // Bord droit
    private static final String CROSS_T_THIN  = "╫";   // Croisement fin-épais
    private static final String CROSS_THIN    = "┼";   // Croisement fin-fin

    // =========================================================================
    //  MÉTHODE PRINCIPALE D'AFFICHAGE
    // =========================================================================

    /**
     * Affiche la grille de Sudoku dans la console avec les séparateurs visuels.
     *
     * @param grid La grille à afficher
     */
    public static void print(SudokuGrid grid) {
        for (int row = 0; row < SudokuGrid.SIZE; row++) {

            // Affichage de la bordure ou du séparateur de ligne approprié
            if (row == 0) {
                printTopBorder();                    // ╔═══╤═══...╗
            } else if (row % SudokuGrid.SUBGRID_SIZE == 0) {
                printThickRowSeparator();            // ╠═══╪═══...╣
            } else {
                printThinRowSeparator();             // ╟───┼───...╢
            }

            // Affichage de la ligne de données
            printDataRow(grid, row);                 // ║ 5 │ 3 │...║
        }

        // Bordure inférieure
        printBottomBorder();                         // ╚═══╧═══...╝
    }

    // =========================================================================
    //  MÉTHODES D'AFFICHAGE DES SÉPARATEURS
    // =========================================================================

    /**
     * Affiche la bordure supérieure de la grille.
     * Exemple : ╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗
     */
    private static void printTopBorder() {
        StringBuilder sb = new StringBuilder();
        sb.append(TOP_LEFT);
        for (int col = 0; col < SudokuGrid.SIZE; col++) {
            sb.append(H_THICK);
            if (col < SudokuGrid.SIZE - 1) {
                // Jonction épaisse entre blocs, fine entre cellules
                sb.append((col + 1) % SudokuGrid.SUBGRID_SIZE == 0 ? TOP_T_THICK : TOP_T_THIN);
            }
        }
        sb.append(TOP_RIGHT);
        System.out.println(sb.toString());
    }

    /**
     * Affiche la bordure inférieure de la grille.
     * Exemple : ╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝
     */
    private static void printBottomBorder() {
        StringBuilder sb = new StringBuilder();
        sb.append(BOTTOM_LEFT);
        for (int col = 0; col < SudokuGrid.SIZE; col++) {
            sb.append(H_THICK);
            if (col < SudokuGrid.SIZE - 1) {
                sb.append((col + 1) % SudokuGrid.SUBGRID_SIZE == 0 ? BOT_T_THICK : BOT_T_THIN);
            }
        }
        sb.append(BOTTOM_RIGHT);
        System.out.println(sb.toString());
    }

    /**
     * Affiche un séparateur épais entre deux blocs 3x3.
     * Exemple : ╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
     */
    private static void printThickRowSeparator() {
        StringBuilder sb = new StringBuilder();
        sb.append(LEFT_THICK);
        for (int col = 0; col < SudokuGrid.SIZE; col++) {
            sb.append(H_THICK);
            if (col < SudokuGrid.SIZE - 1) {
                // Croisement épais-épais entre blocs, épais-fin entre cellules
                sb.append((col + 1) % SudokuGrid.SUBGRID_SIZE == 0 ? CROSS_THICK : CROSS_T_THICK);
            }
        }
        sb.append(RIGHT_THICK);
        System.out.println(sb.toString());
    }

    /**
     * Affiche un séparateur fin entre deux lignes d'un même bloc.
     * Exemple : ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
     */
    private static void printThinRowSeparator() {
        StringBuilder sb = new StringBuilder();
        sb.append(LEFT_THIN);
        for (int col = 0; col < SudokuGrid.SIZE; col++) {
            sb.append(H_THIN);
            if (col < SudokuGrid.SIZE - 1) {
                // Croisement fin-épais entre blocs, fin-fin entre cellules
                sb.append((col + 1) % SudokuGrid.SUBGRID_SIZE == 0 ? CROSS_T_THIN : CROSS_THIN);
            }
        }
        sb.append(RIGHT_THIN);
        System.out.println(sb.toString());
    }

    /**
     * Affiche une ligne de données de la grille.
     * Exemple : ║ 5 │ 3 │   ║   │ 7 │   ║   │   │   ║
     *
     * Les cases vides (valeur 0) sont affichées comme des espaces.
     *
     * @param grid La grille source
     * @param row  L'index de la ligne à afficher (0-8)
     */
    private static void printDataRow(SudokuGrid grid, int row) {
        StringBuilder sb = new StringBuilder();
        sb.append(V_THICK);

        for (int col = 0; col < SudokuGrid.SIZE; col++) {
            int val = grid.getValue(row, col);
            // Affiche le chiffre ou un espace si case vide
            sb.append(val == SudokuGrid.EMPTY ? "   " : " " + val + " ");

            if (col < SudokuGrid.SIZE - 1) {
                // Séparateur épais entre blocs, fin entre cellules
                sb.append((col + 1) % SudokuGrid.SUBGRID_SIZE == 0 ? V_THICK : V_THIN);
            }
        }

        sb.append(V_THICK);
        System.out.println(sb.toString());
    }
}
