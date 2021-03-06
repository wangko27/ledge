// 
//Copyright (c) 2003, 2004 Caltha - Gajda, Krzewski, Mach, Potempski Sp.J. 
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

package org.objectledge.table;

import java.util.HashSet;
import java.util.Set;

/**
 * Table state object represents a state of a tabular data view. It defines a subset of visible
 * rows by providing:
 * <ul>
 * <li>the id of a root node - it may be <code>null</code> for forest and flat 
 * list type of data,</li>
 * <li>expansion state of nodes (does not apply to flat list).</li>
 * </ul>
 *
 * @author <a href="mailto:pablo@caltha.pl">Pawel Potempski</a>
 * @author <a href="mailto:dgajda@caltha.pl">Damian Gajda</a>
 * @version $Id: TableState.java,v 1.12 2007-01-20 13:36:10 pablo Exp $
 */
public class TableState
{
    /** Name under which this table state is mapped in TableService. */
    private String name;

    /** Id under which this table state is mapped in TableService. */
    private int id;

    /** Table root object id,  default: <code>empty string</code>. */
    private String rootId = "";

    /** Expanded all switch, default: <code>false</code>. */
    private boolean allExpanded = false;

    /** Set of expanded nodes ids. */
    private Set<String> expanded = new HashSet<String>();

    /** Name of a column selected for sorting, default: <code>null</code>. */
    private String sortColumnName;

    /** Is current sorting direction ascending, default: <code>true</code>. */
    private boolean ascendingSort = true;

    /** Current table page, default: <code>1</code>. */
    private int currentPage = 1;

    /** Current table page size, default: <code>0</code>. */
    private int pageSize = 0;

    /** Is current table's view tree like, default <code>false</code>. */
    private boolean treeView = false;

    /** ShowRoot switch, default: <code>false</code>, which is good for lists and forests. */
    private boolean showRoot = false;

    /** Maxmimal tree depth of elements visible, default:
     * <code>0</code>, which means any depth. */
    private int maxVisibleDepth = 0;

    /** <code>true</code> if the state object was created during this
     * request. */
    private boolean newState;
    
    /** In tree view whether link to expand/colapse tree should be enabled */
    private boolean expandingEnabled;

	/** 
	 * Constructor for table state.
	 * @param name 
	 * @param id numeric id of a state as assigned by {@link TableStateManager}
	 */
    public TableState(String name, int id)
    {
        this.name = name;
        this.id = id;
        newState = true;
        expandingEnabled = true;
    }

    /**
     * Returns <code>true</code> if the state object was created during this
     * request.
     *
     * <p>Use this method to determine if you need to perform setup steps on
     * the state or not.</p>
     * 
     * @return <code>true</code> if the state has not been modified since its creation
     */
    public boolean isNew()
    {
        return newState;
    }

    /**
     * Returns <code>true</code> if links to expand/collapse tree are active
     * 
     * @return <code>true</code> if links to expand/collapse tree are active
     */
    public boolean isExpandingEnabled()
    {
        return expandingEnabled;
    }

    /** 
     * Set whether links to expand/collapse tree should be active.
     * 
     * @param expandingEnabled <code>true</code> if links should be active.
     */
    public void setExpandingEnabled(boolean expandingEnabled)
    {
        this.expandingEnabled = expandingEnabled;
    }

    /**
     * Called by the table service to mark the state as used previously.
     *
     * <p>You have no interest in calling this method yourself, unless You know what You are doing.</p>
     */
    public void setOld()
    {
        newState = false;
    }

    /**
     * Returns this table state's name as defined by mapping held by 
     * {@link TableStateManager}.
     *
     * @return name of the table state.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns this table state's id as defined by mapping held by 
     * {@link TableStateManager}.
     *
     * @return numerical id of the table state.
     */
    public int getId()
    {
        return id;
    }

    /**
     * Returns the id of the root of the table data.
     *
     * @return the root id.
     */
    public String getRootId()
    {
        return rootId;
    }

    /**
     * Sets root id. Root id will be used to define the subtree displayed as a tree.
     *
     * @param rootId the root id.
     */
    public void setRootId(String rootId)
    {
        this.rootId = rootId;
    }

    /**
     * Checks whether a row with a given id is expanded, rows with empty
     * id are treated as expanded, this allows for easy implementation of
     * flat lists, and forest views.
     *
     * @param rowId id of a checked row
     * @return <code>true</code> if a row with a given id is expanded.
     */
    public boolean isExpanded(String rowId)
    {
        if(rowId.length() == 0 || allExpanded)
        {
            return true;
        }
        else
        {
            return expanded.contains(rowId);
        }
    }

    /**
     * Sets a row's expanded state.
     *
     * @param rowId the id of the row.
     */
    public void setExpanded(String rowId)
    {
        expanded.add(rowId);
    }

    /**
     * Sets expanded state on a number of rows.
     *
     * @param rowIds the ids of rows.
     */
    public void setExpanded(String[] rowIds)
    {
        for(int i=0; i < rowIds.length; i++)
        {
            setExpanded(rowIds[i]);
        }
    }

    /**
     * Clears row's expanded state.
     *
     * @param rowId the id of the row.
     */
    public void setCollapsed(String rowId)
    {
        expanded.remove(rowId);
    }

    /**
     * Toggles a row's expanded state.
     *
     * @param rowId of node
     */
    public void toggleExpanded(String rowId)
    {
        if(expanded.contains(rowId))
        {
            expanded.remove(rowId);
        }
        else
        {
            expanded.add(rowId);
        }
    }

    /**
     * Set expaded all switch.
     *
     * @param allExpanded the all expanded switch.
     */
    public void setAllExpanded(boolean allExpanded)
    {
        this.allExpanded = allExpanded;
    }

    /**
     * Get all expanded state.
     *
     * @return all expanded state.
     */
    public boolean getAllExpanded()
    {
        return allExpanded;
    }

    /**
     * Clears all expanded rows.
     */
    public void clearExpanded()
    {
        expanded.clear();
    }

    /**
     * Returns name of a column selected for sorting.
     *
     * @return name of a column by which table is sorted
     */
    public String getSortColumnName()
    {
        return sortColumnName;
    }

    /**
     * Sets a name of a column selected for sorting.
     *
     * @param sortColumnName name of a column selected for sorting.
     */
    public void setSortColumnName(String sortColumnName)
    {
        this.sortColumnName = sortColumnName;
    }

	/**
	 * Returns current sorting direction.
	 * 
	 * @return <code>true</code> for ascending, <code>false</code> for descending.
	 */
	public boolean getAscSort()
	{
		return ascendingSort;
	}

	/**
	 * Sets the current sorting direction.
	 *
	 * @param ascendingSort <code>true</code> for ascending, <code>false</code> for descending.
	 */
	public void setAscSort(boolean ascendingSort)
	{
		this.ascendingSort = ascendingSort;
	}

    /**
     * Returns the current page.
     *
     * @return the current page.
     */
    public int getCurrentPage()
    {
        return currentPage;
    }

    /**
     * Sets the current page, corrects the value to positive integer.
     *
     * @param currentPage the current page.
     */
    public void setCurrentPage(int currentPage)
    {
        this.currentPage = (currentPage < 1)? 1 : currentPage;
    }

    /**
     * Returns the page size.
     *
     * @return the page size.
     */
    public int getPageSize()
    {
        return pageSize;
    }

    /**
     * Sets the maximal number of pages.
     *
     * @param pageSize the number of pages.
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * Returns current view type.
     * 
     * @return <code>true</code> if the view is tree like, <code>false</code> for list like view.
     */
    public boolean getTreeView()
    {
        return treeView;
    }

    /**
     * Sets the view type for this state.
     *
     * @param treeView <code>true</code> for the tree view, <code>false</code> for list view.
     */
    public void setTreeView(boolean treeView)
    {
        this.treeView = treeView;
    }

    /**
     * Returns the showRoot option.
     *
     * @return <code>true</code> if root node should be included in presented collection of rows.
     */
    public boolean getShowRoot()
    {
        return showRoot;
    }

    /**
     * Sets the showRoot switch.
     *
     * @param showRoot the showRoot switch.
     */
    public void setShowRoot(boolean showRoot)
    {
        this.showRoot = showRoot;
    }

    /** Getter for property maxVisibleDepth, zero value means that there is
     * no limit.
     * @return Value of property maxVisibleDepth.
     */
    public int getMaxVisibleDepth()
    {
        return maxVisibleDepth;
    }

    /** Setter for property maxVisibleDepth.
     * The propery tells maximal deepth of the tree to be shown. No levels below this number will
     * be included in the tree.
     * 
     * @param maxVisibleDepth New value of property maxVisibleDepth.
     */
    public void setMaxVisibleDepth(int maxVisibleDepth)
    {
        this.maxVisibleDepth = maxVisibleDepth;
    }
}
