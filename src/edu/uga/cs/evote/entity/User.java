package edu.uga.cs.evote.entity;


import edu.uga.cs.evote.persistence.Persistable;


/** This is the base class of all registered users of the eVote system.
 * Each user has a name, user name (i.e., a login name), password, and an email address.
 *
 */
public interface User
    extends Persistable
{
    /** Return the user's first name.
     * @return the user's first name
     */
    public String getFirstName();

    /** Set the user's first name.
     * @param firstName the new first name of this user
     */
    public void setFirstName( String firstName );

    /** Return the user's last name.
     * @return the user's last name
     */
    public String getLastName();

    /** Set the user's first name.
     * @param lastName the new last name of this user
     */
    public void setLastName( String lastName );

    /** Return the user's user name (login name).
     * @return the user's user name (login name)
     */
    public String getUserName();

    /** Set the user's user name (login name).
     * @param userName the new user (login name)
     */
    public void   setUserName( String userName );

    /** Return the user's password.
     * @return the user's password
     */
    public String getPassword();

    /** Set the user's password.
     * @param password the new password
     */
    public void   setPassword( String password );

    /** Return the user's email address.
     * @return the user's email address
     */
    public String getEmailAddress();

    /** Set the user's email address.
     * @param emailAddress the new email address
     */
    public void   setEmailAddress( String emailAddress );

    /** Return the user's address.
     * @return the user's address
     */
    public String getAddress();

    /** Set the user's address.
     * @param address the new address
     */
    public void   setAddress( String address );

}
