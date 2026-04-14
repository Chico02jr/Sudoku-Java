import java.io.*;
import java.util.Scanner;

/**
 * Représente une grille de Sudoku 9x9.
 *
 * Cette classe est responsable de :
 *  - Stocker les valeurs de la grille
 *  - Charger la grille depuis un fichier texte ou la saisie console
 *  - Valider la cohérence de la grille (pas de doublons)
 *
 * Format du fichier attendu :
 *  - 9 lignes, chacune contenant 9 chiffres entre 0 et 9 séparés par des espaces
 *  - Le chiffre 0 représente une case vide
 *
 * @author Groupe 13
 */
public class SudokuGrid {

    /** Taille de la grille (9x9) */
    public static final int SIZE = 9;

    /** Taille d'une sous-grille (3x3) */
    public static final int SUBGRID_SIZE = 3;

    /** Valeur représentant une case vide */
    public static final int EMPTY = 0;

    /** La grille de jeu */
    private int[][] grid;

    /**
     * Constructeur : initialise une grille vide 9x9.
     */
    public SudokuGrid() {
        grid = new int[SIZE][SIZE];
    }

    // =========================================================================
    //  CHARGEMENT DE LA GRILLE
    // =========================================================================

    /**
     * Charge la grille depuis un fichier texte.
     *
     * @param filename Le chemin vers le fichier
     * @throws SudokuException si le fichier est introuvable, mal formaté ou invalide
     */
    public void loadFromFile(String filename) throws SudokuException {
        File file = new File(filename);

        // Vérification de l'existence du fichier
        if (!file.exists()) {
            throw new SudokuException("Fichier introuvable : " + filename);
        }

        // Lecture et parsing du fichier
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            parseGrid(reader);
        } catch (IOException e) {
            throw new SudokuException("Erreur lors de la lecture du fichier : " + e.getMessage());
        }

        // Validation de la cohérence de la grille
        validateGrid();
    }

    /**
     * Charge la grille depuis la saisie manuelle de l'utilisateur.
     *
     * @param scanner Le scanner pour lire l'entrée console
     * @throws SudokuException si le format saisi est invalide
     */
    public void loadFromConsole(Scanner scanner) throws SudokuException {
        System.out.println("Saisissez la grille ligne par ligne.");
        System.out.println("Format : 9 chiffres séparés par des espaces (0 = case vide)");
        System.out.println("Exemple : 5 3 0 0 7 0 0 0 0");
        System.out.println();

        for (int row = 0; row < SIZE; row++) {
            System.out.print("Ligne " + (row + 1) + " : ");
            String line = scanner.nextLine().trim();
            parseRow(line, row);
        }

        // Validation de la cohérence de la grille
        validateGrid();
    }

    // =========================================================================
    //  PARSING
    // =========================================================================

    /**
     * Parse le contenu complet du fichier et remplit la grille.
     *
     * @param reader Le lecteur du fichier
     * @throws IOException      En cas d'erreur d'entrée/sortie
     * @throws SudokuException  Si le nombre de lignes est incorrect
     */
    private void parseGrid(BufferedReader reader) throws IOException, SudokuException {
        int rowCount = 0;
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue; // Ignorer les lignes vides

            if (rowCount >= SIZE) {
                throw new SudokuException(
                    "Format invalide : trop de lignes dans le fichier (attendu " + SIZE + ").");
            }

            parseRow(line, rowCount);
            rowCount++;
        }

        if (rowCount != SIZE) {
            throw new SudokuException(
                "Format invalide : " + rowCount + " ligne(s) trouvée(s), " + SIZE + " attendues.");
        }
    }

    /**
     * Parse une ligne et remplit la ligne correspondante dans la grille.
     *
     * @param line La ligne à parser
     * @param row  L'index de la ligne dans la grille (0-8)
     * @throws SudokuException Si le nombre de valeurs ou leur format est incorrect
     */
    private void parseRow(String line, int row) throws SudokuException {
        String[] parts = line.trim().split("\\s+");

        // Vérification du nombre de colonnes
        if (parts.length != SIZE) {
            throw new SudokuException(
                "Format invalide à la ligne " + (row + 1) + " : " +
                parts.length + " valeur(s) trouvée(s), " + SIZE + " attendues.");
        }

        for (int col = 0; col < SIZE; col++) {
            try {
                int value = Integer.parseInt(parts[col]);

                // Vérification de la plage des valeurs
                if (value < 0 || value > 9) {
                    throw new SudokuException(
                        "Valeur invalide '" + value + "' à la ligne " + (row + 1) +
                        ", colonne " + (col + 1) + ". Les valeurs doivent être entre 0 et 9.");
                }

                grid[row][col] = value;

            } catch (NumberFormatException e) {
                throw new SudokuException(
                    "Valeur non numérique '" + parts[col] + "' à la ligne " + (row + 1) +
                    ", colonne " + (col + 1) + ".");
            }
        }
    }

    // =========================================================================
    //  VALIDATION
    // =========================================================================

    /**
     * Vérifie la cohérence de la grille :
     *  - Pas de doublons dans chaque ligne
     *  - Pas de doublons dans chaque colonne
     *  - Pas de doublons dans chaque sous-grille 3x3
     *
     * @throws SudokuException Si un doublon est détecté
     */
    private void validateGrid() throws SudokuException {
        // Vérification des lignes
        for (int row = 0; row < SIZE; row++) {
            boolean[] seen = new boolean[SIZE + 1];
            for (int col = 0; col < SIZE; col++) {
                int val = grid[row][col];
                if (val != EMPTY) {
                    if (seen[val]) {
                        throw new SudokuException(
                            "Grille invalide : doublon du chiffre " + val +
                            " détecté à la ligne " + (row + 1) + ".");
                    }
                    seen[val] = true;
                }
            }
        }

        // Vérification des colonnes
        for (int col = 0; col < SIZE; col++) {
            boolean[] seen = new boolean[SIZE + 1];
            for (int row = 0; row < SIZE; row++) {
                int val = grid[row][col];
                if (val != EMPTY) {
                    if (seen[val]) {
                        throw new SudokuException(
                            "Grille invalide : doublon du chiffre " + val +
                            " détecté à la colonne " + (col + 1) + ".");
                    }
                    seen[val] = true;
                }
            }
        }

        // Vérification des sous-grilles 3x3
        for (int boxRow = 0; boxRow < SUBGRID_SIZE; boxRow++) {
            for (int boxCol = 0; boxCol < SUBGRID_SIZE; boxCol++) {
                boolean[] seen = new boolean[SIZE + 1];
                for (int r = 0; r < SUBGRID_SIZE; r++) {
                    for (int c = 0; c < SUBGRID_SIZE; c++) {
                        int val = grid[boxRow * SUBGRID_SIZE + r][boxCol * SUBGRID_SIZE + c];
                        if (val != EMPTY) {
                            if (seen[val]) {
                                throw new SudokuException(
                                    "Grille invalide : doublon du chiffre " + val +
                                    " dans la sous-grille (" + (boxRow + 1) + ", " + (boxCol + 1) + ").");
                            }
                            seen[val] = true;
                        }
                    }
                }
            }
        }
    }

    // =========================================================================
    //  GETTERS ET SETTERS
    // =========================================================================

    /**
     * Retourne la valeur à la position (row, col).
     *
     * @param row La ligne (0-8)
     * @param col La colonne (0-8)
     * @return La valeur de la case (0 si vide)
     */
    public int getValue(int row, int col) {
        return grid[row][col];
    }

    /**
     * Définit la valeur à la position (row, col).
     *
     * @param row   La ligne (0-8)
     * @param col   La colonne (0-8)
     * @param value La valeur à placer (0-9)
     */
    public void setValue(int row, int col, int value) {
        grid[row][col] = value;
    }
}
