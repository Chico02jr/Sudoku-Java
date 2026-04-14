/**
 * Solveur de Sudoku basé sur l'algorithme de Backtracking (retour sur trace).
 *
 * Principe de l'algorithme :
 *  1. Trouver la première case vide de la grille.
 *  2. Essayer les chiffres de 1 à 9 dans cette case.
 *  3. Si un chiffre est valide (respecte les règles du Sudoku), le placer et
 *     continuer récursivement vers la case vide suivante.
 *  4. Si aucun chiffre n'est valide, effacer la case (backtrack) et revenir
 *     à la case précédente pour essayer le chiffre suivant.
 *  5. Si aucune case vide n'est trouvée, la grille est résolue.
 *
 * Complexité : O(9^n) dans le pire cas, où n est le nombre de cases vides.
 * En pratique, très rapide pour des grilles de Sudoku standard.
 *
 * @author Groupe 13
 */
public class SudokuSolver {

    /** La grille à résoudre */
    private SudokuGrid grid;

    /**
     * Constructeur.
     *
     * @param grid La grille de Sudoku à résoudre
     */
    public SudokuSolver(SudokuGrid grid) {
        this.grid = grid;
    }

    // =========================================================================
    //  RÉSOLUTION
    // =========================================================================

    /**
     * Lance la résolution de la grille par backtracking.
     *
     * @return true si une solution a été trouvée, false sinon
     */
    public boolean solve() {
        return backtrack();
    }

    /**
     * Algorithme de backtracking récursif.
     *
     * À chaque appel :
     *  - Cherche la première case vide
     *  - Essaie les chiffres 1 à 9
     *  - Valide le placement selon les règles du Sudoku
     *  - Rappelle récursivement ou revient en arrière si nécessaire
     *
     * @return true si la grille est entièrement résolue, false s'il faut revenir en arrière
     */
    private boolean backtrack() {
        // Étape 1 : Chercher la prochaine case vide
        int[] emptyCell = findEmptyCell();

        // Si aucune case vide n'est trouvée → la grille est résolue !
        if (emptyCell == null) {
            return true;
        }

        int row = emptyCell[0];
        int col = emptyCell[1];

        // Étape 2 : Essayer les chiffres de 1 à 9
        for (int num = 1; num <= SudokuGrid.SIZE; num++) {

            if (isValid(row, col, num)) {
                // Étape 3 : Placer le chiffre (tentative)
                grid.setValue(row, col, num);

                // Étape 4 : Appel récursif pour la suite
                if (backtrack()) {
                    return true; // Solution trouvée !
                }

                // Étape 5 : Backtrack → le chiffre ne mène pas à une solution
                grid.setValue(row, col, SudokuGrid.EMPTY);
            }
        }

        // Aucun chiffre valide trouvé pour cette case → on remonte
        return false;
    }

    // =========================================================================
    //  MÉTHODES UTILITAIRES
    // =========================================================================

    /**
     * Trouve la première case vide de la grille (parcours ligne par ligne).
     *
     * @return Un tableau [row, col] indiquant la position de la case vide,
     *         ou null si aucune case vide n'existe (grille complète)
     */
    private int[] findEmptyCell() {
        for (int row = 0; row < SudokuGrid.SIZE; row++) {
            for (int col = 0; col < SudokuGrid.SIZE; col++) {
                if (grid.getValue(row, col) == SudokuGrid.EMPTY) {
                    return new int[]{row, col};
                }
            }
        }
        return null; // Grille complète
    }

    /**
     * Vérifie si un chiffre peut légalement être placé à la position (row, col).
     * Un placement est valide si le chiffre n'est pas déjà présent dans :
     *  - La même ligne
     *  - La même colonne
     *  - La même sous-grille 3x3
     *
     * @param row La ligne cible (0-8)
     * @param col La colonne cible (0-8)
     * @param num Le chiffre à tester (1-9)
     * @return true si le placement est valide
     */
    private boolean isValid(int row, int col, int num) {
        return !isInRow(row, num)
            && !isInCol(col, num)
            && !isInSubGrid(row, col, num);
    }

    /**
     * Vérifie si un chiffre est déjà présent dans une ligne.
     *
     * @param row La ligne à vérifier
     * @param num Le chiffre recherché
     * @return true si le chiffre est déjà dans la ligne
     */
    private boolean isInRow(int row, int num) {
        for (int col = 0; col < SudokuGrid.SIZE; col++) {
            if (grid.getValue(row, col) == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si un chiffre est déjà présent dans une colonne.
     *
     * @param col La colonne à vérifier
     * @param num Le chiffre recherché
     * @return true si le chiffre est déjà dans la colonne
     */
    private boolean isInCol(int col, int num) {
        for (int row = 0; row < SudokuGrid.SIZE; row++) {
            if (grid.getValue(row, col) == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si un chiffre est déjà présent dans la sous-grille 3x3
     * à laquelle appartient la case (row, col).
     *
     * @param row La ligne de la case
     * @param col La colonne de la case
     * @param num Le chiffre recherché
     * @return true si le chiffre est déjà dans la sous-grille
     */
    private boolean isInSubGrid(int row, int col, int num) {
        // Calcul du coin supérieur gauche de la sous-grille
        int startRow = (row / SudokuGrid.SUBGRID_SIZE) * SudokuGrid.SUBGRID_SIZE;
        int startCol = (col / SudokuGrid.SUBGRID_SIZE) * SudokuGrid.SUBGRID_SIZE;

        for (int r = 0; r < SudokuGrid.SUBGRID_SIZE; r++) {
            for (int c = 0; c < SudokuGrid.SUBGRID_SIZE; c++) {
                if (grid.getValue(startRow + r, startCol + c) == num) {
                    return true;
                }
            }
        }
        return false;
    }
}
