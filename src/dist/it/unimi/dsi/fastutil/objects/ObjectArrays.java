

/* Generic definitions */




/* Assertions (useful to generate conditional code) */
/* Current type and class (and size, if applicable) */
/* Value methods */
/* Interfaces (keys) */
/* Interfaces (values) */
/* Abstract implementations (keys) */
/* Abstract implementations (values) */
/* Static containers (keys) */
/* Static containers (values) */
/* Implementations */
/* Synchronized wrappers */
/* Unmodifiable wrappers */
/* Other wrappers */
/* Methods (keys) */
/* Methods (values) */
/* Methods (keys/values) */
/* Methods that have special names depending on keys (but the special names depend on values) */
/* Equality */
/* Object/Reference-only definitions (keys) */
/* Object/Reference-only definitions (values) */
/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2002-2008 Sebastiano Vigna 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package it.unimi.dsi.fastutil.objects;
import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.Hash;
/** A class providing static methods and objects that do useful things with type-specific arrays.
 *
 * In particular, the <code>ensureCapacity()</code>, <code>grow()</code>,
 * <code>trim()</code> and <code>setLength()</code> methods allow to handle
 * arrays much like array lists. This can be very useful when efficiency (or
 * syntactic simplicity) reasons make array lists unsuitable.
 *
 * <P><strong>Warning:</strong> creating arrays 
 * using {@linkplain java.lang.reflect.Array#newInstance(Class,int) reflection}, as it
 * happens in {@link #ensureCapacity(Object[],int,int)} and {@link #grow(Object[],int,int)},
 * is <em>significantly slower</em> than using <code>new</code>. This phenomenon is particularly
 * evident in the first growth phases of an array reallocated with doubling (or similar) logic.
 *
 * @see java.util.Arrays
 */
public class ObjectArrays {


 /** The inverse of the golden ratio times 2<sup>16</sup>. */
 public static final long ONEOVERPHI = 106039;

 private ObjectArrays() {}

 /** A static, final, empty array. */
 public final static Object[] EMPTY_ARRAY = {};



 /** Creates a new array using a the given one as prototype. 
	 *
	 * <P>This method returns a new array of the given length whose element
	 * are of the same class as of those of <code>prototype</code>. In case
	 * of an empty array, it tries to return {@link #EMPTY_ARRAY}, if possible.
	 *
	 * @param prototype an array that will be used to type the new one.
	 * @param length the length of the new array.
	 * @return a new array of given type and length.
	 */

 @SuppressWarnings("unchecked")
 private static <K> K[] newArray( final K[] prototype, final int length ) {
  final Class componentType = prototype.getClass().getComponentType();
  if ( length == 0 && componentType == Object.class ) return (K[])EMPTY_ARRAY;
  return (K[])java.lang.reflect.Array.newInstance( prototype.getClass().getComponentType(), length );
 }


 /** Ensures that an array can contain the given number of entries.
	 *
	 * <P>If you cannot foresee whether this array will need again to be
	 * enlarged, you should probably use <code>grow()</code> instead.
	 *
	 * @param array an array.
	 * @param length the new minimum length for this array.
	 * @return <code>array</code>, if it contains <code>length</code> entries or more; otherwise,
	 * an array with <code>length</code> entries whose first <code>array.length</code>
	 * entries are the same as those of <code>array</code>.
	 */
 public static <K> K[] ensureCapacity( final K[] array, final int length ) {
  if ( length > array.length ) {
   final K t[] =

    newArray( array, length );



   System.arraycopy( array, 0, t, 0, array.length );
   return t;
  }
  return array;
 }

 /** Ensures that an array can contain the given number of entries, preserving just a part of the array.
	 *
	 * @param array an array.
	 * @param length the new minimum length for this array.
	 * @param preserve the number of elements of the array that must be preserved in case a new allocation is necessary.
	 * @return <code>array</code>, if it can contain <code>length</code> entries or more; otherwise,
	 * an array with <code>length</code> entries whose first <code>preserve</code>
	 * entries are the same as those of <code>array</code>.
	 */
 public static <K> K[] ensureCapacity( final K[] array, final int length, final int preserve ) {
  if ( length > array.length ) {
   final K t[] =

    newArray( array, length );



   System.arraycopy( array, 0, t, 0, preserve );
   return t;
  }
  return array;
 }

 /** Grows the given array to the maximum between the given length and
	 * the current length divided by the golden ratio, provided that the given
	 * length is larger than the current length.
	 *
	 * <P> Dividing by the golden ratio (&phi;) approximately increases the array
	 * length by 1.618. If you want complete control on the array growth, you
	 * should probably use <code>ensureCapacity()</code> instead.
	 *
	 * @param array an array.
	 * @param length the new minimum length for this array.
	 * @return <code>array</code>, if it can contain <code>length</code>
	 * entries; otherwise, an array with
	 * max(<code>length</code>,<code>array.length</code>/&phi;) entries whose first
	 * <code>array.length</code> entries are the same as those of <code>array</code>.
	 * */

 public static <K> K[] grow( final K[] array, final int length ) {
  if ( length > array.length ) {
   final int newLength = (int)Math.min( Math.max( ( ONEOVERPHI * array.length ) >>> 16, length ), Integer.MAX_VALUE );
   final K t[] =

    newArray( array, newLength );



   System.arraycopy( array, 0, t, 0, array.length );
   return t;
  }
  return array;
 }

 /** Grows the given array to the maximum between the given length and
	 * the current length divided by the golden ratio, provided that the given
	 * length is larger than the current length, preserving just a part of the array.
	 *
	 * <P> Dividing by the golden ratio (&phi;) approximately increases the array
	 * length by 1.618. If you want complete control on the array growth, you
	 * should probably use <code>ensureCapacity()</code> instead.
	 *
	 * @param array an array.
	 * @param length the new minimum length for this array.
	 * @param preserve the number of elements of the array that must be preserved in case a new allocation is necessary.
	 * @return <code>array</code>, if it can contain <code>length</code>
	 * entries; otherwise, an array with
	 * max(<code>length</code>,<code>array.length</code>/&phi;) entries whose first
	 * <code>preserve</code> entries are the same as those of <code>array</code>.
	 * */

 public static <K> K[] grow( final K[] array, final int length, final int preserve ) {

  if ( length > array.length ) {
   final int newLength = (int)Math.min( Math.max( ( ONEOVERPHI * array.length ) >>> 16, length ), Integer.MAX_VALUE );

   final K t[] =

    newArray( array, newLength );



   System.arraycopy( array, 0, t, 0, preserve );

   return t;
  }
  return array;

 }

 /** Trims the given array to the given length.
	 *
	 * @param array an array.
	 * @param length the new maximum length for the array.
	 * @return <code>array</code>, if it contains <code>length</code>
	 * entries or less; otherwise, an array with
	 * <code>length</code> entries whose entries are the same as
	 * the first <code>length</code> entries of <code>array</code>.
	 * 
	 */

 public static <K> K[] trim( final K[] array, final int length ) {
  if ( length >= array.length ) return array;
  final K t[] =

   newArray( array, length );



  System.arraycopy( array, 0, t, 0, length );
  return t;
 }

 /** Sets the length of the given array.
	 *
	 * @param array an array.
	 * @param length the new length for the array.
	 * @return <code>array</code>, if it contains exactly <code>length</code>
	 * entries; otherwise, if it contains <em>more</em> than
	 * <code>length</code> entries, an array with <code>length</code> entries
	 * whose entries are the same as the first <code>length</code> entries of
	 * <code>array</code>; otherwise, an array with <code>length</code> entries
	 * whose first <code>array.length</code> entries are the same as those of
	 * <code>array</code>.
	 * 
	 */

 public static <K> K[] setLength( final K[] array, final int length ) {
  if ( length == array.length ) return array;
  if ( length < array.length ) return trim( array, length );
  return ensureCapacity( array, length );
 }

 /** Returns a copy of a portion of an array.
	 *
	 * @param array an array.
	 * @param offset the first element to copy.
	 * @param length the number of elements to copy.
	 * @return a new array containing <code>length</code> elements of <code>array</code> starting at <code>offset</code>.
	 */

 public static <K> K[] copy( final K[] array, final int offset, final int length ) {
  ensureOffsetLength( array, offset, length );
  final K[] a =

   newArray( array, length );



  System.arraycopy( array, offset, a, 0, length );
  return a;
 }

 /** Returns a copy of an array.
	 *
	 * @param array an array.
	 * @return a copy of <code>array</code>.
	 */

 public static <K> K[] copy( final K[] array ) {
  return array.clone();
 }

 /** Fills the given array with the given value.
	 *
	 * <P>This method uses a backward loop. It is significantly faster than the corresponding
	 * method in {@link java.util.Arrays}.
	 *
	 * @param array an array.
	 * @param value the new value for all elements of the array.
	 */

 public static <K> void fill( final K[] array, final K value ) {
  int i = array.length;
  while( i-- != 0 ) array[ i ] = value;
 }

 /** Fills a portion of the given array with the given value.
	 *
	 * <P>If possible (i.e., <code>from</code> is 0) this method uses a
	 * backward loop. In this case, it is significantly faster than the
	 * corresponding method in {@link java.util.Arrays}.
	 *
	 * @param array an array.
	 * @param from the starting index of the portion to fill.
	 * @param to the end index of the portion to fill.
	 * @param value the new value for all elements of the specified portion of the array.
	 */

 public static <K> void fill( final K[] array, final int from, int to, final K value ) {
  ensureFromTo( array, from, to );
  if ( from == 0 ) while( to-- != 0 ) array[ to ] = value;
  else for( int i = from; i < to; i++ ) array[ i ] = value;
 }



 /** Returns true if the two arrays are elementwise equal.
	 *
	 * <P>This method uses a backward loop. It is significantly faster than the corresponding
	 * method in {@link java.util.Arrays}.
	 *
	 * @param a1 an array.
	 * @param a2 another array.
	 * @return true if the two arrays are of the same length, and their elements are equal.
	 */

 public static <K> boolean equals( final K[] a1, final K a2[] ) {
  int i = a1.length;
  if ( i != a2.length ) return false;
  while( i-- != 0 ) if (! ( (a1[ i ]) == null ? (a2[ i ]) == null : (a1[ i ]).equals(a2[ i ]) ) ) return false;
  return true;
 }




 /** Ensures that a range given by its first (inclusive) and last (exclusive) elements fits an array.
	 *
	 * <P>This method may be used whenever an array range check is needed.
	 *
	 * @param a an array.
	 * @param from a start index (inclusive).
	 * @param to an end index (inclusive).
	 * @throws IllegalArgumentException if <code>from</code> is greater than <code>to</code>.
	 * @throws ArrayIndexOutOfBoundsException if <code>from</code> or <code>to</code> are greater than the array length or negative.
	 */
 public static <K> void ensureFromTo( final K[] a, final int from, final int to ) {
  Arrays.ensureFromTo( a.length, from, to );
 }

 /** Ensures that a range given by an offset and a length fits an array.
	 *
	 * <P>This method may be used whenever an array range check is needed.
	 *
	 * @param a an array.
	 * @param offset a start index.
	 * @param length a length (the number of elements in the range).
	 * @throws IllegalArgumentException if <code>length</code> is negative.
	 * @throws ArrayIndexOutOfBoundsException if <code>offset</code> is negative or <code>offset</code>+<code>length</code> is greater than the array length.
	 */
 public static <K> void ensureOffsetLength( final K[] a, final int offset, final int length ) {
  Arrays.ensureOffsetLength( a.length, offset, length );
 }


 /** A type-specific content-based hash strategy for arrays. */

 private static final class ArrayHashStrategy <K> implements Hash.Strategy<K[]>, java.io.Serializable {
     public static final long serialVersionUID = -7046029254386353129L;

  public int hashCode( final K[] o ) {
   return java.util.Arrays.hashCode( o );
  }

  public boolean equals( final K[] a, final K[] b ) {
   return ObjectArrays.equals( a, b );
  }
 }

 /** A type-specific content-based hash strategy for arrays.
	 *
	 * <P>This hash strategy may be used in custom hash collections whenever keys are
	 * arrays, and they must be considered equal by content. This strategy
	 * will handle <code>null</code> correctly, and it is serializable.
	 */

 @SuppressWarnings("unchecked")
 public final static Hash.Strategy<Object[]> HASH_STRATEGY = new ArrayHashStrategy();


}
