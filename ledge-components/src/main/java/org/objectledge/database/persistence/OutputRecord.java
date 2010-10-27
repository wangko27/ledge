// 
// Copyright (c) 2003, Caltha - Gajda, Krzewski, Mach, Potempski Sp.J. 
// All rights reserved. 
// 
// Redistribution and use in source and binary forms, with or without modification,  
// are permitted provided that the following conditions are met: 
//  
// * Redistributions of source code must retain the above copyright notice,  
//	 this list of conditions and the following disclaimer. 
// * Redistributions in binary form must reproduce the above copyright notice,  
//	 this list of conditions and the following disclaimer in the documentation  
//	 and/or other materials provided with the distribution. 
// * Neither the name of the Caltha - Gajda, Krzewski, Mach, Potempski Sp.J.  
//	 nor the names of its contributors may be used to endorse or promote products  
//	 derived from this software without specific prior written permission. 
// 
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED  
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
// IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,  
// INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,  
// BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
// OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,  
// WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)  
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE  
// POSSIBILITY OF SUCH DAMAGE. 
// 
package org.objectledge.database.persistence;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.Date;

/**
 * An interface that exposes minimal set methods necessary for storing Persistent object 
 * field information into JDBC database entry.
 * 
 * @author <a href="mailto:rafal@caltha.pl">Rafal Krzewski</a>
 * @version $Id: OutputRecord.java,v 1.7 2004-12-27 04:43:24 rafal Exp $
 */
public interface OutputRecord
{
    /**
     * Sets a <code>boolean</code> field value.
     *
     * @param field the name of the field.
     * @param value the value of the field.
     * @throws PersistenceException if the field could not be set to the 
     *         specified value.
     */
    public abstract void setBoolean(String field, boolean value) throws PersistenceException;
    /**
     * Sets a <code>byte</code> field value.
     *
     * @param field the name of the field.
     * @param value the value of the field.
     * @throws PersistenceException if the field could not be set to the 
     * specified value.
     */
    public abstract void setByte(String field, byte value) throws PersistenceException;
    /**
     * Sets a <code>short</code> field value.
     *
     * @param field the name of the field.
     * @param value the value of the field.
     * @throws PersistenceException if the field could not be set to the 
     *         specified value.
     */
    public abstract void setShort(String field, short value) throws PersistenceException;
    /**
     * Sets an <code>int</code> field value.
     *
     * @param field the name of the field.
     * @param value the value of the field.
     * @throws PersistenceException if the field could not be set to the 
     *         specified value.
     */
    public abstract void setInteger(String field, int value) throws PersistenceException;
    /**
     * Sets a <code>long</code> field value.
     *
     * @param field the name of the field.
     * @param value the value of the field.
     * @throws PersistenceException if the field could not be set to the 
     *         specified value.
     */
    public abstract void setLong(String field, long value) throws PersistenceException;
    /**
     * Sets a <code>BigDecimal</code> field value.
     *
     * @param field the name of the field.
     * @param value the value of the field.
     * @throws PersistenceException if the field could not be set to the 
     *         specified value.
     */
    public abstract void setBigDecimal(String field, BigDecimal value) throws PersistenceException;
    /**
     * Sets a <code>float</code> field value.
     *
     * @param field the name of the field.
     * @param value the value of the field.
     * @throws PersistenceException if the field could not be set to the 
     *         specified value.
     */
    public abstract void setFloat(String field, float value) throws PersistenceException;
    /**
     * Sets a <code>double</code> field value.
     *
     * @param field the name of the field.
     * @param value the value of the field.
     * @throws PersistenceException if the field could not be set to the 
     *         specified value.
     */
    public abstract void setDouble(String field, double value) throws PersistenceException;
    /**
     * Sets a <code>String</code> field value.
     *
     * @param field the name of the field.
     * @param value the value of the field.
     * @throws PersistenceException if the field could not be set to the
     *         specified value. 
     */
    public abstract void setString(String field, String value) throws PersistenceException;
    /**
     * Sets a <code>byte</code> array field value.
     *
     * <p>String value read from the database will be BASE64 decoded to obtain
     * byte array.</p>
     *
     * @param field the name of the field.
     * @param value the value of the field.
     * @throws PersistenceException if the field could not be set to the
     *         specified value. 
     */
    public abstract void setBytes(String field, byte[] value) throws PersistenceException;
    /**
     * Sets a <code>Date</code> field value.
     *
     * @param field the name of the field.
     * @param value the value of the field.
     * @throws PersistenceException if the field could not be set to the
     *         specified value. 
     */
    public abstract void setDate(String field, Date value) throws PersistenceException;
    /**
     * Sets a <code>Time</code> field value.
     * 
     * @param field the name of the field.
     * @param value the value of the filed.
     * @throws PersistenceException if the field could not be set to the
     *         specified value. 
     */
    public abstract void setTime(String field, Date value) throws PersistenceException;
    /**
     * Sets a <code>Timestamp</code> field value.
     * 
     * @param field the name of the field.
     * @param value the value of the filed.
     * @throws PersistenceException if the field could not be set to the
     *         specified value. 
     */
    public abstract void setTimestamp(String field, Date value) throws PersistenceException;
    /**
     * Sets a <code>Array</code> field value.
     * 
     * @param field the name of the field.
     * @param value the value of the filed.
     * @throws PersistenceException if the field could not be set to the
     *         specified value. 
     */
    public abstract void setArray(String field, Array value) throws PersistenceException;
    /**
     * Sets a <code>Blob</code> field value.
     * 
     * @param field the name of the field.
     * @param value the value of the filed.
     * @throws PersistenceException if the field could not be set to the
     *         specified value. 
     */
    public abstract void setBlob(String field, Blob value) throws PersistenceException;
    /**
     * Sets a <code>Clob</code> field value.
     * 
     * @param field the name of the field.
     * @param value the value of the filed.
     * @throws PersistenceException if the field could not be set to the
     *         specified value. 
     */
    public abstract void setClob(String field, Clob value) throws PersistenceException;
    /**
     * Sets a <code>Ref</code> field value.
     * 
     * @param field the name of the field.
     * @param value the value of the filed.
     * @throws PersistenceException if the field could not be set to the
     *         specified value. 
     */
    public abstract void setRef(String field, Ref value) throws PersistenceException;
    /**
     * Sets a <code>URL</code> field value.
     * 
     * @param field the name of the field.
     * @param value the value of the filed.
     * @throws PersistenceException if the field could not be set to the
     *         specified value. 
     */
    public abstract void setURL(String field, URL value) throws PersistenceException;
    /**
     * Sets a <code>Object</code> field value.
     * 
     * @param field the name of the field.
     * @param value the value of the filed.
     * @throws PersistenceException if the field could not be set to the
     *         specified value. 
     */
    public abstract void setObject(String field, Object value) throws PersistenceException;
    /**
     * Sets a field to <code>SQL NULL</code> value.
     *
     * @param field the name of the field.
     * @throws PersistenceException if the field could not be set to the
     *         specified value. 
     */
    public abstract void setNull(String field) throws PersistenceException;
    
    // Implementation specific ///////////////////////////////////////////
    
    /**
     * Get the where clasuse.
     * 
     * @return the clause.
     * @throws PersistenceException if occured.
     */
    public abstract String getWhereClause() throws PersistenceException;
    
    /**
     * Builds an insert statement with contained data.
     *
     * @param conn database connection.
     * @return the statement.
     * @throws PersistenceException if the statement could not be built.
     * @throws SQLException if the statement could not be created.
     */
    public abstract PreparedStatement getInsertStatement(Connection conn)
        throws PersistenceException, SQLException;
    /**
     * Builds an update statement with contained data.
     *
     * @param conn database connection.
     * @return the statement.
     * @throws PersistenceException if the statement could not be built.
     * @throws SQLException if the statement could not be created.
     */
    public abstract PreparedStatement getUpdateStatement(Connection conn)
        throws PersistenceException, SQLException;
    /**
     * Builds a <code>DELETE</code> statement with contained data.
     *
     *
     * @param conn database connection.
     * @return the statement.
     * @throws PersistenceException if the statement could not be built.
     * @throws SQLException if the statement could not be created.
     */
    public abstract PreparedStatement getDeleteStatement(Connection conn)
        throws PersistenceException, SQLException;
    /**
     * Returns a value of a field.
     *
     * <p>Note! String and Date values will be returned enclosed in single
     * quotes, byte array values will be returned BASE64 encoded and enclosed
     * in single quotes.</p>
     *
     * @param name the name of the field
     * @return stringied and possibly quoted value of the field, or
     *         <code>null</code> if unset.
     */
    public abstract String getField(String name);
}