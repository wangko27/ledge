// 
//Copyright (c) 2003, Caltha - Gajda, Krzewski, Mach, Potempski Sp.J. 
//All rights reserved. 
// 
//Redistribution and use in source and binary forms, with or without modification,  
//are permitted provided that the following conditions are met: 
//  
//* Redistributions of source code must retain the above copyright notice,  
//this list of conditions and the following disclaimer. 
//* Redistributions in binary form must reproduce the above copyright notice,  
//this list of conditions and the following disclaimer in the documentation  
//and/or other materials provided with the distribution. 
//* Neither the name of the Caltha - Gajda, Krzewski, Mach, Potempski Sp.J.  
//nor the names of its contributors may be used to endorse or promote products  
//derived from this software without specific prior written permission. 
// 
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  
//AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED  
//WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
//IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,  
//INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,  
//BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
//OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,  
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)  
//ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE  
//POSSIBILITY OF SUCH DAMAGE. 
// 

package org.objectledge.table.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.objectledge.table.ExtendedTableModel;
import org.objectledge.table.TableColumn;
import org.objectledge.table.TableRow;
import org.objectledge.table.TableState;


/**
 * This class provides a base implementation of a TableRowSet interface.
 * It ensures that rows collection is built only once.
 *
 * @author <a href="mailto:dgajda@caltha.pl">Damian Gajda</a>
 * @version $Id: BaseGenericRowSet.java,v 1.2 2004-02-11 13:42:11 zwierzem Exp $
 */
public abstract class BaseGenericRowSet extends BaseRowSet
{
    /** Table model which provides data for this row set. */
    protected ExtendedTableModel model;

    /** Root row for this row set. */
    protected TableRow rootRow;

    /** Keeps total number of rows in this row set. */
    protected int totalRowCount;

    /** Local row set cache - keeps page of rows. */
    protected TableRow[] rows;

    /** This map allows quick child row lookup.
     * Used in {@link #hasMoreChildren(TableRow,TableRow)}. */
    protected HashMap rowsByParent = new HashMap();

    /** This map allows quick parent row lookup.
     * Used in {@link #hasMoreChildren(TableRow,TableRow)} and {@link
     * #getParentRow(TableRow)}. */
    protected HashMap rowsByChild = new HashMap();

    /** This map allows quick row lookup by it's id.
     * May be useful in some subclasses.
     */
    protected HashMap rowsById = new HashMap();

    /**
     * construct the object
     *
     * @param state the state of the table instance
     * @param model the table model
     */
    public BaseGenericRowSet(TableState state, ExtendedTableModel model)
    {
        super(state);
        this.model = model;
    }

    /**
     * Returns an list of {@link TableRow} objects, which represents
     * a view of TableModel data defined by TableState object.
     *
     * @return a list of rows.
     */
    public TableRow[] getRows()
    {
        if(rows == null)
        {
            List list = getAllRows();

            list = getCurrentPageRows(list);

            rows = new TableRow[list.size()];
            rows = (TableRow[])list.toArray(rows);
        }

        return rows;
    }

    /**
     * Returns the row by it's id.
     *
     * @param id id of the row
     * @return the row
     */
    public TableRow getRowById(String id)
    {
        // in case rows were not drawn from the model
        getRows();
        return (TableRow)rowsById.get(id);
    }

    /**
     * Gets the root node of the row set.
     */
    public TableRow getRootRow()
    {
        // in case rows were not drawn from the model
        getRows();
        return rootRow;
    }

    /**
     * Returns the parent row of the row
     *
     * @param state the state of the table
     * @param row the child row
     * @return the parent row
     */
    public TableRow getParentRow(TableRow row)
    {
        // in case rows were not drawn from the model
        getRows();
        return (TableRow)rowsByChild.get(row);
    }

    /**
     * Checks whether the ancestor has more children
     *
     * @return <code>true</code> if has more children
     */
    public boolean hasMoreChildren(TableRow ancestorRow, TableRow descendantRow)
    {
        // in case rows were not drawn from the model
        getRows();

        if(descendantRow == ancestorRow)
        {
            throw new IllegalStateException("Ancestor and descendant rows are the same object");
        }

        // get a direct child of ancestor which is also an ancestor of descendant
        TableRow childRow = null;
        while(descendantRow != null && descendantRow != ancestorRow)
        {
            childRow = descendantRow;
            descendantRow = (TableRow)rowsByChild.get(descendantRow);
        }

        if(descendantRow == null)
        {
            throw new IllegalStateException("Ancestor is not a real ancestor of a given row");
        }

        // get filtered and sorted ancestor's children
        TableRow[] children = (TableRow[])rowsByParent.get(ancestorRow);

        // check whether child that was found is on the end of children list
        TableRow lastChildRow = children[children.length-1];
        return (childRow != lastChildRow);
    }

    /**
     * Return the number of elements in returned array.
     *
     * @return the size of returned array
     */
    public int getPageRowCount()
    {
        // in case rows were not drawn from the model
        TableRow[] list = getRows();
        return list.length;
    }

    /**
     * Return the total number of rows in this row set.
     *
     * @return total number of rows
     */
    public int getTotalRowCount()
    {
        // in case rows were not drawn from the model
        getRows();
        return totalRowCount;
    }

    // implementation ////////////////////////////////////////////////////////

    /**
     * Returns a sublist of of a given list which corresponds to a view
     * of table state's current page in this row set.
     */
    protected List getCurrentPageRows(List list)
    {
        int page = state.getCurrentPage();
        int perPage = state.getPageSize();

        if(page > 0 && perPage > 0)
        {
            int start = (page-1)*perPage; // inclusive
            int end = page*perPage;		  // exclusive
            int listSize = list.size();

            // automatic page number reset
            // TODO: Page sanitization here?????
            if(start >= listSize)
            {
                int numPages = listSize / perPage;
                numPages += (listSize % perPage > 0) ? 1: 0;
                start = (numPages-1)*perPage;
                start = (start < 0)? 0: start;
                end = start + perPage;
                state.setCurrentPage(start/perPage+1);
            }
            end = (end < listSize)? end: listSize;

            list = list.subList(start, end);
        }

        return list;
    }

    /**
     * Returns the list of {@link TableRow} objects representing the object tree or list.
     *
     * @return a list of tree nodes.
     */
    protected List getAllRows()
    {
        // start row list creation
        ArrayList rowList = new ArrayList();
        // WARN: save root row
        this.rootRow = getSubTree(state.getRootId(), 0, rowList); // depth = 0

        // sort rows collection for list view
        sortRows(rowList);

        // WARN: initialise total row count
        this.totalRowCount = rowList.size();

        return rowList;
    }

    /**
     * Builds tree nodes for the specified subtree and stores them in the
     * target list.
     *
     * @param node the root node of the subtree.
     * @param depth the nesting depth of the subtree root.
     * @param target the target node list.
     */
    protected TableRow getSubTree(String rootId, int depth, List rowList)
    {
        Object rootObject = model.getObject(rootId);

        //1. get children for this subtree root node
        Object[] childrenObjects = model.getChildren(rootObject);

        // 1.1. filter them out
        List childrenList = new ArrayList();
        for(int i = 0; i< childrenObjects.length; i++)
        {
            if(accept(childrenObjects[i]))
            {
                childrenList.add(childrenObjects[i]);
            }
        }

        // -------------------

        // 2.0. decide whether to continue recursion
        // CONTINUE if depth is not too big AND if root node is expanded
        boolean continueRecursion = (checkDepth(depth+1) && expanded(rootId));

        // 2.1. create current rootRow
        int childCount = childrenList.size();
        int visibleChildCount = continueRecursion ? childrenList.size() : 0;
        TableRow rootRow = new TableRow(rootId, rootObject, depth, childCount, visibleChildCount);

        // 2.2. add current root row to rowList
        if(rootObject != null)
        {
            // check if a MAIN root node should be shown (depth==0 and state.getShowRoot()==true)
            if(depth > 0 || state.getShowRoot())
            {
                rowList.add(rootRow);
            }
        }

        // 2.3. map root row by it's id
        rowsById.put(rootId, rootRow);

        // -------------------

        if(continueRecursion)
        {
            // 3.0. increase depth
            depth++;

            // 3.1. sort children collection for tree or forest view
            sortChildren(childrenList);

            // WARN: create TableRow array for children caching
            TableRow[] children = new TableRow[childrenList.size()];

            // 4. add children to rowList collection
            for(int i = 0; i< childrenList.size(); i++)
            {
                Object childObject = childrenList.get(i);
                String childId = model.getId(childObject);

                // go down the tree
                TableRow childRow = getSubTree(childId, depth, rowList);

                // WARN: add TableRow to array created for children caching
                children[i] = childRow;
                // WARN: cache childRow's parent row
                rowsByChild.put(childRow, rootRow);
            }

            // WARN: cache this rows children
            rowsByParent.put(rootRow, children);
        }

        // -------------------

        return rootRow;
    }

    // utility methods ///////////////////////////////////////////////////////

    /**
     * Check whether the object with a given id is expanded.
     * Tree and List view imeplementations will differ.
     *
     * @param id the id of an object to be check for being expaned.
     * @return <code>true</code> if expanded.
     */
    protected abstract boolean expanded(String id);

    /**
     *   Sorts rows collection for list view
     *
     * @param rowsList list of table rows for current view.
     */
    protected abstract void sortRows(List rowsList);

    /**
     *   Sorts children collection for tree or forest view
     *
     * @param childrenList list of children nodes for current subtree.
     */
    protected abstract void sortChildren(List childrenList);

    /**
     * Returns the selected sorting column.
     *
     * <p>If no sort column was chosen, <code>null</code> will be
     * returned.</p>
     *
     * @return the selected sorting column, or <code>null</code>
     */
    protected TableColumn getSortColumn()
    {
        String sortColumnName = state.getSortColumnName();
        TableColumn column = null;
        TableColumn[] columns = model.getColumns();
        for(int i=0; i<columns.length; i++)
        {
            if(columns[i].getName().equals(sortColumnName))
            {
                column = columns[i];
                break;
            }
        }
        return column;
    }
}
