/**
 * Exception personnalisée pour les erreurs liées au Sudoku.
 * Utilisée pour signaler les problèmes de chargement, de format ou de validité de la grille.
 *
 * @author Groupe 13
 */
public class SudokuException extends Exception {

    /**
     * Constructeur avec message d'erreur.
     * @param message Description de l'erreur rencontrée
     */
    public SudokuException(String message) {
        super(message);
    }
}
