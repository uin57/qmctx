
package ket.kdb;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;

final class RangeTree<T extends RangeIndexed> extends AbstractCollection<T>
{
	private enum Color { red, black };
	private final class Node
	{				
		public Node() // only for Nil
		{	
			left = this;
			right = this;
			p = this;
			c = Color.black;
		}
		
		public Node(final T obj) // only for insert new node
		{
			this.obj = obj;
			left = Nil;
			right = Nil;
			p = Nil;
			c = Color.red;
		}
		
		public int key()
		{
			return obj.range().from;
		}
		
		public boolean contains(final int pageIndex)
		{
			return obj.range().contains(pageIndex);
		}
		
		public boolean isRed()
		{
			return c == Color.red;
		}
		
		public boolean isBlack()
		{
			return c == Color.black;
		}
		
		public void setRed()
		{
			c = Color.red;
		}
		
		public void setBlack()
		{
			c = Color.black;
		}
		
		public void setRight(final Node node)
		{
			right = node;
			if( node != Nil )
				node.p = this;
		}
		
		public void setLeft(final Node node)
		{
			left = node;
			if( node != Nil )
				node.p = this;
		}		
		
		public T obj;
		public Node left;
		public Node right;
		public Node p;
		public Color c;
	}
	
	@Override
	public final boolean add(final T obj)
	{
		Node node = new Node(obj);
		if( root == Nil )
			root = node;
		else
			insert(root, node);
		insertFixup(node);
		++pbSize;
		pageCount += obj.range().size();
		return true;
	}
	
	public final int getPageCount()
	{
		return pageCount;
	}
	
	@Override
	public final int size()
	{
		return pbSize;
	}
	
	@Override
	public final void clear()
	{
		root = Nil;
		pbSize = 0;
		pageCount = 0;
	}
	
	public final int height()
	{
		return height(root);
	}
	
	public boolean contains(final int pageIndex)
	{
		return get(pageIndex) != null;
	}
	
	public T get(final int pageIndex)
	{
		return get(root, pageIndex).obj;
	}
	
	private void remove(final Node node)
	{
		Node y = ( node.right == Nil || node.left == Nil ) ? node : suc(node);
		Node x = ( y.right != Nil ) ? y.right : y.left;
		x.p = y.p;
		if( x.p == Nil )
			root = x;
		else if( y == y.p.left )
			x.p.left = x;
		else
			x.p.right = x;
		if( y != node )
			node.obj = y.obj;
		if( y.isBlack() )
			removeFixup(x);
	}
	
	private void removeFixup(Node node) // node.p == y.p
	{
		while( node.p != Nil && node.isBlack() )
		{
			if( node == node.p.left )
			{
				Node w = node.p.right;
				if( w.isRed() )
				{
					w.setBlack();
					node.p.setRed();
					leftRotate(node.p);
					w = node.p.right;
				}
				if( w.left.isBlack() && w.right.isBlack() )
				{
					w.setRed();
					node = node.p;
				}
				else
				{
					if( w.right.isBlack() )
					{
						w.left.setBlack();
						w.setRed();
						rightRotate(w);
						w = node.p.right;
					}
					w.c = node.p.c;
					node.p.setBlack();
					w.right.setBlack();
					leftRotate(node.p);
					node = root;
				}
			}
			else if( node == node.p.right )
			{
				Node w = node.p.left;
				if( w.isRed() )
				{
					w.setBlack();
					node.p.setRed();
					rightRotate(node.p);
					w = node.p.left;
				}
				if( w.right.isBlack() && w.left.isBlack() )
				{
					w.setRed();
					node = node.p;
				}
				else
				{
					if( w.left.isBlack() )
					{
						w.right.setBlack();
						w.setRed();
						leftRotate(w);
						w = node.p.left;
					}
					w.c = node.p.c;
					node.p.setBlack();
					w.left.setBlack();
					rightRotate(node.p);
					node = root;
				}
			}
		}
		node.setBlack();
	}
	
	@Override
	public final boolean contains(final Object obj)
	{
		Node node = get(root, ((RangeIndexed)obj).range().from);
		return node.obj == obj;
	}
	
	@Override
	public final boolean remove(final Object obj)
	{
		Node node = get(root, ((RangeIndexed)obj).range().from);
		if( node.obj != obj)
			return false;
		pageCount -= node.obj.range().size();
		remove(node);
		--pbSize;
		return true;
	}
	
	private int height(Node node)
	{
		if( node == Nil )
			return -1;
		return 1 + Math.max(height(node.left), height(node.right));
	}
	
	private Node first(Node node)
	{
		if( node == Nil )
			return Nil;
		
		while( node.left != Nil )
			node = node.left;
		return node;
	}
	
	private Node suc(Node node)
	{
		if( node.right != Nil )
			return first(node.right);
		while( node.p != Nil && node.p.right == node )
			node = node.p;
		return node.p;
	}
	
	private void leftRotate(final Node node) // node != Nil && node.right != Nil
	{
		Node up = node.right;
		// up
		up.p = node.p;
		if( up.p == Nil )
			root = up;
		else if( up.p.right == node )
			up.p.right = up;
		else
			up.p.left = up;
		
		node.setRight(up.left);
		up.setLeft(node);
	}
	
	private void rightRotate(final Node node) // node != Nil && node.left != Nil
	{
		Node up = node.left;
		// up
		up.p = node.p;
		if( up.p == Nil )
			root = up;
		else if( up.p.right == node )
			up.p.right = up;
		else
			up.p.left = up;
		
		node.setLeft(up.right);
		up.setRight(node);
	}
	
	private boolean testConsistency(final Node node)
	{
		if( node.left != Nil )
		{
			if( node.left.key() > node.key() )
				return false;
			if( node.isRed() && node.left.isRed() )
				return false;
			if( ! testConsistency(node.left) )
				return false;
		}
		if( node.right != Nil )
		{
			if( node.right.key() < node.key() )
				return false;
			if( node.isRed() && node.right.isRed() )
				return false;
			if( ! testConsistency(node.right) )
				return false;
		}
		return true;
	}	

	private Node get(Node where, final int pageIndex)
	{
		while( where != Nil && ! where.contains(pageIndex) )
		{
			where = pageIndex > where.key() ? where.right : where.left;
		}
		return where;
	}
	
	private void insert(Node where, final Node node)
	{
		while( true )
		{
			int keyDiff = node.key() - where.key();
			if( node.obj.range().intersect(where.obj.range()) )
				throw new Error("BufferMapError, dup key " + node.obj.range());
			if( keyDiff > 0 )
			{
				if( where.right == Nil )
				{
					where.setRight(node);
					break;
				}
				else
					where = where.right;
			}
			else
			{
				if( where.left == Nil )
				{
					where.setLeft(node);
					break;
				}
				else
					where = where.left;
			}
		}
	}
	
	private void insertFixup(Node node)
	{
		while( node.p.isRed() )
		{
			Node p = node.p;
			if( p == p.p.left )
			{
				Node y = p.p.right;
				if( y.isRed() )
				{
					y.setBlack();
					p.setBlack();
					p.p.setRed();
					node = p.p;
				}
				else 
				{
					if( node == p.right )
					{
						node = p;
						leftRotate(node);
						p = node.p;
					}
					p.setBlack();
					p.p.setRed();
					rightRotate(p.p);
				}
			}
			else if( p == p.p.right )
			{
				Node y = p.p.left;
				if( y.isRed() )
				{
					y.setBlack();
					p.setBlack();
					p.p.setRed();
					node = p.p;
				}
				else 
				{
					if( node == p.left )
					{
						node = p;
						rightRotate(node);
						p = node.p;
					}
					p.setBlack();
					p.p.setRed();
					leftRotate(p.p);
				}
			}
		}
		root.setBlack();
	}
	
	private class IteratorImp implements Iterator<T>
	{
		@Override
		public boolean hasNext()
		{
			return curNode != Nil;
		}

		@Override
		public T next()
		{
			if( curNode == Nil )
				throw new NoSuchElementException();
			
			T obj = curNode.obj;
			curNode = suc(curNode);
			return obj;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
		
		private Node curNode = first(root);
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return new IteratorImp();
	}
	
	public boolean testConsistency()
	{
		return root == Nil || testConsistency(root);
	}
	
	private final Node Nil = new Node();
	private Node root = Nil;
	private int pbSize;
	private int pageCount;
}
