import java.util.Scanner;

/**
 * Point d'entrée du programme Solveur de Sudoku.
 *
 * Ce programme résout une grille de Sudoku 9x9 en utilisant l'algorithme
 * de Backtracking. Il affiche la grille initiale et la grille résolue
 * avec un formatage console soigné.
 *
 * Usage :
 *   java Main grille.txt   → Charge la grille depuis le fichier 'grille.txt'
 *   java Main              → L'utilisateur saisit la grille manuellement
 *
 * Format du fichier d'entrée :
 *   9 lignes de 9 chiffres séparés par des espaces (0 = case vide)
 *   Exemple :
 *     5 3 0 0 7 0 0 0 0
 *     6 0 0 1 9 5 0 0 0
 *     ...
 *
 * @author Groupe 13
 */
public class Main {

    public static void main(String[] args) {

        SudokuGrid grid = new SudokuGrid();

        // -----------------------------------------------------------------
        // ÉTAPE 1 : Chargement de la grille
        // -----------------------------------------------------------------
        if (args.length > 0) {
            // Chargement depuis le fichier fourni en argument
            String filename = args[0];
            System.out.println("Chargement de la grille depuis : " + filename);

            try {
                grid.loadFromFile(filename);
                System.out.println("Grille chargée avec succès.\n");
            } catch (SudokuException e) {
                System.err.println("[ERREUR] " + e.getMessage());
                System.exit(1);
            }

        } else {
            // Saisie manuelle si aucun fichier n'est fourni
            Scanner scanner = new Scanner(System.in);
            try {
                grid.loadFromConsole(scanner);
                System.out.println("Grille saisie avec succès.\n");
            } catch (SudokuException e) {
                System.err.println("[ERREUR] " + e.getMessage());
                System.exit(1);
            } finally {
                scanner.close();
            }
        }

        // -----------------------------------------------------------------
        // ÉTAPE 2 : Affichage de la grille initiale
        // -----------------------------------------------------------------
        System.out.println("┌─────────────────────────┐");
        System.out.println("│      GRILLE INITIALE     │");
        System.out.println("└─────────────────────────┘");
        SudokuDisplay.print(grid);

        // -----------------------------------------------------------------
        // ÉTAPE 3 : Résolution par Backtracking
        // -----------------------------------------------------------------
        System.out.println("\nRésolution en cours...");
        long startTime = System.currentTimeMillis();

        SudokuSolver solver = new SudokuSolver(grid);
        boolean solved = solver.solve();

        long elapsed = System.currentTimeMillis() - startTime;

        // -----------------------------------------------------------------
        // ÉTAPE 4 : Affichage du résultat
        // -----------------------------------------------------------------
        if (solved) {
            System.out.println("Résolution réussie en " + elapsed + " ms.\n");
            System.out.println("┌─────────────────────────┐");
            System.out.println("│      GRILLE RÉSOLUE      │");
            System.out.println("└─────────────────────────┘");
            SudokuDisplay.print(grid);
        } else {
            System.out.println("\n[ATTENTION] Aucune solution trouvée pour cette grille.");
            System.out.println("Vérifiez que la grille initiale est valide et résolvable.");
        }
    }
}
