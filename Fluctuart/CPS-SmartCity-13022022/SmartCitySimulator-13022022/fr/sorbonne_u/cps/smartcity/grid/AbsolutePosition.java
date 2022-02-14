package fr.sorbonne_u.cps.smartcity.grid;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a
// basic component programming model to program with components
// distributed applications in the Java programming language.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability. 
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or 
// data to be ensured and,  more generally, to use and operate it in the 
// same conditions as regards security. 
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.

import java.io.Serializable;

// -----------------------------------------------------------------------------
/**
 * The class <code>AbsolutePosition</code> implements a position in the smart
 * city.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * Positions are defined by coordinates in a grid of streets and avenues
 * crossing at 90 degrees. As position are meant to be either on streets
 * or avenues, at least one of the coordinate must be integral to be on a
 * street or on an avenue.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	{@code AbsolutePosition.inGrid(getX())}
 * invariant	{@code AbsolutePosition.inGrid(getY())}
 * invariant	{@code AbsolutePosition.onGrid(getX()) || AbsolutePosition.onGrid(getY())} 
 * </pre>
 * 
 * <p>Created on : 2021-12-23</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			AbsolutePosition
implements	Serializable
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	
	private static final long serialVersionUID = 1L;
	/** north to south coordinate (streets).								*/
	protected final double	x;
	/** west to east coordinate (avenues).									*/
	protected final double	y;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	
	/**
	 * create an absolute position, either on a street or on an avenue.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code AbsolutePosition.inGrid(x) && AbsolutePosition.inGrid(y)}
	 * pre	{@code AbsolutePosition.onGrid(x) || AbsolutePosition.onGrid(y)}
	 * post	{@code getX() == x && getY() == y}
	 * </pre>
	 *
	 * @param x		north to south coordinate (streets).
	 * @param y		west to east coordinate (avenues).
	 */
	public				AbsolutePosition(double x, double y)
	{
		super();

		assert	AbsolutePosition.inGrid(x) && AbsolutePosition.inGrid(y);
		assert	AbsolutePosition.onGrid(x) || AbsolutePosition.onGrid(y);

		this.x = x;
		this.y = y;
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------
	
	/**
	 * return true if {@code v} in in the smart city grid, false otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param v	coordinate to be tested.
	 * @return	true if {@code v} is in the smart city grid, false otherwise.
	 */
	public static boolean	inGrid(double v)
	{
		return v >= 0.0 && v <= GridCity.N;
	}

	/**
	 * return true if {@code v} in on the smart city grid, false otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param v	coordinate to be tested.
	 * @return	true if {@code v} in on the smart city grid, false otherwise.
	 */
	public static boolean	onGrid(double v)
	{
		return inGrid(v) && v == Math.floor(v);
	}

	/**
	 * return the x coordinate of this position.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	{@code inGrid(return)}
	 * </pre>
	 *
	 * @return	the x coordinate of this position.
	 */
	public double		getX()
	{
		return this.x;
	}

	/**
	 * return the y coordinate of this position.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	{@code inGrid(return)}
	 * </pre>
	 *
	 * @return	the y coordinate of this position.
	 */
	public double		getY()
	{
		return this.y;
	}

	/**
	 * return true if this position is equal to {@code p}.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param p	another position.
	 * @return	true if this position is equal to {@code p}.
	 */
	public boolean		equalAbsolutePosition(AbsolutePosition p)
	{
		return this.x == p.x && this.y == p.y;
	}

	/**
	 * return the distance between this point and {@code p}.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param p	another position.
	 * @return	the distance between this point and {@code p}.
	 */
	public double		distance(AbsolutePosition p)
	{
		double dx = this.getX() - p.getX();
		double dy = this.getY() - p.getY();
		return Math.sqrt(dx*dx + dy*dy);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean		equals(Object obj)
	{
		if (obj.getClass().equals(this.getClass())) {
			return this.equalAbsolutePosition((AbsolutePosition) obj);
		}
		return false;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String		toString()
	{
		StringBuffer sb =
				new StringBuffer(this.getClass().getSimpleName() + "[");
		this.generateStringContent(sb);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * generate a string representation of the content of the position.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code sb != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param sb	a string buffer in which to add.
	 */
	protected void		generateStringContent(StringBuffer sb)
	{
		sb.append(this.getX());
		sb.append(", ");
		sb.append(this.getY());
	}
}
// -----------------------------------------------------------------------------
