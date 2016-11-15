package abgabe03;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Mieke Narjes, Luka Hartwig, David Hoeck
 *
 * @param <E>
 */
public abstract class NFA<E> {

	private Pattern language;


	public NFA<E> complement() {
		NFA<E> other = getComplement();
		assert (this.intersection(other).isEmpty());
		return other;
	}

	public NFA<E> intersection(NFA<E> nfa) {
		NFA<E> other = getIntersection(nfa);
		// TODO check correctness of intersection
		return other;
	}

	
	/**
	 * 
	 * method to get the complement of the NFA
	 */
	protected abstract NFA<E> getComplement();

	/**
	 * 
	 *method to get the intersection of two NFA's.
	 * 
	 */
	protected abstract NFA<E> getIntersection(NFA<E> nfa);

	/**
	 * 
	 * method to check if the language of an 
	 */
	public abstract boolean isEmpty();

	public boolean includes(NFA<E> nfa) {
		return this.getComplement().getIntersection(nfa).isEmpty();
	}

	private boolean checkWord(String word) {
		Matcher matcher = language.matcher(word);
		assert (matcher.matches());
		return matcher.matches();
	}

	public Pattern getLanguage() {
		return language;
	}

}
