package jms.helpers.tests;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.AccessController;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import sun.security.util.SecurityConstants;

import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class JmsTests
{
	public static void main(String[] args)
	{
		try
		{
			JmsTests tests = new JmsTests();

//			FilePermission fperm = new FilePermission("C:\\WINDOWS\\System32\\LG.Interceptor\\*", "read");

//			tests.genericToGeneric();
//			tests.genericToGenericReplyTo();
//			tests.u1_genericToGeneric();
			tests.u2_genericToBrokerAware();
//			tests.u3_brokerAwareToGeneric();
//			tests.genericToUnmanagedService();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	private ServerInteraction start(int testNumber)
	{
		ServerInteraction si = ServerInteraction.begin();

		JmsTestClientInteraction.computeSender(si, testNumber);
		JmsTestClientInteraction.computeIDs(si);

		si.setPeerType(DisplayType.NO_PEER);

		return si;
	}

	private ServerInteraction startEx(int testNumber)
	{
		ServerInteraction si = ServerInteraction.begin();

		JmsTestClientInteraction.computeSender(si, testNumber);
		JmsTestClientInteraction.computeIDs(si);

		si.setPeerAddr("pluto.actional.com");

		return si;
	}

	private void u1_genericToGeneric() throws Exception
	{
		final int testNb = 1;
		final String testStr = "u" + testNb;

		JmsFakeMessage msg = new JmsFakeMessage(testStr);

		ServerInteraction si = start(testNb);

		JmsTestClientInteraction intr1 = new JmsTestClientInteraction(msg);

		intr1.clientSendsToBroker(null);

		si.end();

		JmsTestClientInteraction intr2 = new JmsTestClientInteraction(msg);

		intr2.setConsumer(testNb);
		intr2.clientReceivesFromBroker(null);
	}

	private void u2_genericToBrokerAware() throws Exception
	{
		final int testNb = 2;
		final String testStr = "u" + testNb + '-';

		final JmsFakeMessage msg = new JmsFakeMessage(testStr + "queue");

		ServerInteraction si = start(testNb);

		JmsTestClientInteraction intr1 = new JmsTestClientInteraction(msg);

		intr1.clientSendsToBroker(null);

		si.end();

		JmsTestClientInteraction intr2 = new JmsTestClientInteraction(msg, testStr + "broker3", testStr + "broker-machine3")
		{
			protected void doClientReceivesFromBroker() throws Exception
			{
				JmsTestClientInteraction intr2 = new JmsTestClientInteraction(msg)
				{
					protected void doClientReceivesFromBroker() throws Exception
					{
						super.doClientReceivesFromBroker();
					}
				};

				intr2.setConsumer(testNb);
				intr2.clientReceivesFromBroker(null);
			}
		};

		intr2.setConsumer(testNb);
		intr2.clientReceivesFromBroker(null);
	}

	private void u3_brokerAwareToGeneric() throws Exception
	{
		final int testNb = 3;
		final String testStr = "u" + testNb + '-';
		final JmsFakeMessage msg = new JmsFakeMessage(testStr + "queue");

		ServerInteraction si = start(testNb);

		JmsTestClientInteraction intr1 = new JmsTestClientInteraction(msg)
		{
			protected void doClientSendsToBroker() throws Exception
			{
				JmsTestClientInteraction intr1 = new JmsTestClientInteraction(msg,
						testStr + "broker1", testStr + "broker-machine1" + testNb);

				intr1.clientSendsToBroker(null);
			}
		};

		intr1.clientSendsToBroker(null);

		si.end();

		JmsTestClientInteraction intr2 = new JmsTestClientInteraction(msg);

		intr2.setConsumer(testNb);
		intr2.clientReceivesFromBroker(null);
	}

	private void genericToGenericReplyTo() throws Exception
	{
		final int testNb = 2;
		final String testStr = "test" + testNb;

		JmsFakeMessage msg = new JmsFakeMessage(testStr, testStr + "a");

		ServerInteraction si = start(testNb);

		JmsTestClientInteraction intr1 = new JmsTestClientInteraction(msg);

		intr1.clientSendsToBroker(null);

		si.end();

		JmsTestClientInteraction intr2 = new JmsTestClientInteraction(msg)
		{
			protected void doClientReceivesFromBroker() throws Exception
			{
				JmsFakeMessage msg1 = new JmsFakeMessage(testStr + "b");

				JmsTestClientInteraction intr1 = new JmsTestClientInteraction(msg1);

				intr1.clientSendsToBroker(null);

				JmsFakeMessage msg2 = new JmsFakeMessage(testStr + "a");

				JmsTestClientInteraction intr2 = new JmsTestClientInteraction(msg2);

				intr2.clientSendsToBroker(null);
			}
		};

		intr2.setConsumer(testNb);
		intr2.clientReceivesFromBroker(null);
	}

	private void genericToUnmanagedService() throws Exception
	{
		final int testNb = 4;

//		genericToUnmanagedService(testNb, "test.jmstransport.text" + testNb);

//		genericToUnmanagedService(testNb, "TEST.JMSTRANSPORT.TEXT" + testNb);

		genericToUnmanagedService(testNb, "queue://QM_mr_vm_wmq/TEST.JMSTRANSPORT.TEXT" + testNb);
	}

	private void genericToUnmanagedService(final int testNb, final String testStr) throws Exception
	{
		final String replyTo = testStr + JmsFakeMessage.TEMP_PATTERN + System.currentTimeMillis();

		ServerInteraction si = startEx(testNb);

		JmsFakeMessage msg1 = new JmsFakeMessage(testStr, replyTo);
		JmsTestClientInteraction intr1 = new JmsTestClientInteraction(msg1);
		intr1.clientSendsToBroker(null);

		JmsFakeMessage msg2 = new JmsFakeMessage(replyTo);
		JmsTestClientInteraction intr2 = new JmsTestClientInteraction(msg2, intr1.getCorrReqRespBytes());
		intr2.setConsumer(testNb);
		intr2.clientReceivesFromBroker(null);

		si.end();
	}
}

class FilePermission extends Permission implements Serializable {

	    /**
	     * Execute action.
	     */
	    private final static int EXECUTE = 0x1;
	    /**
	     * Write action.
	     */
	    private final static int WRITE   = 0x2;
	    /**
	     * Read action.
	     */
	    private final static int READ    = 0x4;
	    /**
	     * Delete action.
	     */
	    private final static int DELETE  = 0x8;

	    /**
	     * All actions (read,write,execute,delete)
	     */
	    private final static int ALL     = READ|WRITE|EXECUTE|DELETE;
	    /**
	     * No actions.
	     */
	    private final static int NONE    = 0x0;

	    // the actions mask
	    private transient int mask;

	    // does path indicate a directory? (wildcard or recursive)
	    private transient boolean directory;

	    // is it a recursive directory specification?
	    private transient boolean recursive;

	    /**
	     * the actions string.
	     *
	     * @serial
	     */
	    private String actions; // Left null as long as possible, then
	                            // created and re-used in the getAction function.

	    // canonicalized dir path. In the case of
	    // directories, it is the name "/blah/*" or "/blah/-" without
	    // the last character (the "*" or "-").

	    private transient String cpath;

	    // static Strings used by init(int mask)
	    private static final char RECURSIVE_CHAR = '-';
	    private static final char WILD_CHAR = '*';

	/*
	    public String toString()
	    {
	        StringBuffer sb = new StringBuffer();
		sb.append("***\n");
		sb.append("cpath = "+cpath+"\n");
		sb.append("mask = "+mask+"\n");
		sb.append("actions = "+getActions()+"\n");
		sb.append("directory = "+directory+"\n");
		sb.append("recursive = "+recursive+"\n");
		sb.append("***\n");
		return sb.toString();
	    }
	*/

	    private static final long serialVersionUID = 7930732926638008763L;

	    /**
	     * initialize a FilePermission object. Common to all constructors.
	     * Also called during de-serialization.
	     *
	     * @param mask the actions mask to use.
	     *
	     */
	    private void init(int mask)
	    {

		if ((mask & ALL) != mask)
			throw new IllegalArgumentException("invalid actions mask");

		if (mask == NONE)
			throw new IllegalArgumentException("invalid actions mask");

		if ((cpath = getName()) == null)
			throw new NullPointerException("name can't be null");

		this.mask = mask;

		if (cpath.equals("<<ALL FILES>>")) {
		    directory = true;
		    recursive = true;
		    cpath = "";
		    return;
		}

		int len = cpath.length();
		char last = ((len > 0) ? cpath.charAt(len - 1) : 0);

		if (last == RECURSIVE_CHAR &&
		    (len == 1 || cpath.charAt(len - 2) == File.separatorChar)) {
		    directory = true;
		    recursive = true;
		    cpath = cpath.substring(0, --len);
		} else if (last == WILD_CHAR &&
		    (len == 1 || cpath.charAt(len - 2) == File.separatorChar)) {
		    directory = true;
		    //recursive = false;
		    cpath = cpath.substring(0, --len);
		} else {
		    // overkill since they are initialized to false, but
		    // commented out here to remind us...
		    //directory = false;
		    //recursive = false;
		}

		if (len == 0) {
		    cpath = (String) java.security.AccessController.doPrivileged(
			       new sun.security.action.GetPropertyAction("user.dir"));
		}

		// store only the canonical cpath if possible

		// need a doPrivileged block as getCanonicalPath
		// might attempt to access user.dir to turn a relative
		// path into an absolute path.
		cpath = (String)
//		    AccessController.doPrivileged(
				new java.security.PrivilegedAction() {
		    public Object run() {
			try {
			    File file = new File(cpath);
			    String canonical_path = file.getCanonicalPath();
			    int ln;
			    if (directory &&
		  		((ln=canonical_path.length()) == 0 ||
				canonical_path.charAt(ln - 1) != File.separatorChar)) {
				return canonical_path + File.separator;
			    } else {
				return canonical_path;
			    }
			} catch (IOException ioe) {
			    // ignore if we can't canonicalize path?
				ioe.printStackTrace();
			}
			return cpath;
		    }
		}.run();

		// XXX: at this point the path should be absolute. die if it isn't?
	    }

	    /**
	     * Creates a new FilePermission object with the specified actions.
	     * <i>path</i> is the pathname of a
	     * file or directory, and <i>actions</i> contains a comma-separated list of the
	     * desired actions granted on the file or directory. Possible actions are
	     * "read", "write", "execute", and "delete".
	     *
	     * <p>A pathname that ends in "/*" (where "/" is
	     * the file separator character, <code>File.separatorChar</code>) indicates
	     * a directory and all the files contained in that directory. A pathname
	     * that ends with "/-" indicates a directory and (recursively) all files
	     * and subdirectories contained in that directory. The special pathname
	     * "&lt;&lt;ALL FILES&gt;&gt;" matches all files.
	     *
	     * <p>A pathname consisting of a single "*" indicates all the files
	     * in the current directory, while a pathname consisting of a single "-"
	     * indicates all the files in the current directory and
	     * (recursively) all files and subdirectories contained in the current
	     * directory.
	     *
	     * @param path the pathname of the file/directory.
	     * @param actions the action string.
	     */

	    public FilePermission(String path, String actions)
	    {
		super(path);
		init(getMask(actions));
	    }

	    /**
	     * Creates a new FilePermission object using an action mask.
	     * More efficient than the FilePermission(String, String) constructor.
	     * Can be used from within
	     * code that needs to create a FilePermission object to pass into the
	     * <code>implies</code> method.
	     *
	     * @param path the pathname of the file/directory.
	     * @param mask the action mask to use.
	     */

	    // package private for use by the FilePermissionCollection add method
	    FilePermission(String path, int mask)
	    {
		super(path);
		init(mask);
	    }

	    /**
	     * Checks if this FilePermission object "implies" the specified permission.
	     * <P>
	     * More specifically, this method returns true if:<p>
	     * <ul>
	     * <li> <i>p</i> is an instanceof FilePermission,<p>
	     * <li> <i>p</i>'s actions are a proper subset of this
	     * object's actions, and <p>
	     * <li> <i>p</i>'s pathname is implied by this object's
	     *      pathname. For example, "/tmp/*" implies "/tmp/foo", since
	     *      "/tmp/*" encompasses the "/tmp" directory and all files in that
	     *      directory, including the one named "foo".
	     * </ul>
	     * @param p the permission to check against.
	     *
	     * @return true if the specified permission is implied by this object,
	     * false if not.
	     */
	    public boolean implies(Permission p) {
		if (!(p instanceof FilePermission))
		    return false;

		FilePermission that = (FilePermission) p;

		// we get the effective mask. i.e., the "and" of this and that.
		// They must be equal to that.mask for implies to return true.

		return ((this.mask & that.mask) == that.mask) && impliesIgnoreMask(that);
	    }

	    /**
	     * Checks if the Permission's actions are a proper subset of the
	     * this object's actions. Returns the effective mask iff the
	     * this FilePermission's path also implies that FilePermission's path.
	     *
	     * @param that the FilePermission to check against.
	     * @param exact return immediatly if the masks are not equal
	     * @return the effective mask
	     */
	    boolean impliesIgnoreMask(FilePermission that) {
		if (this.directory) {
		    if (this.recursive) {
			// make sure that.path is longer then path so
			// something like /foo/- does not imply /foo
			if (that.directory) {
			    return (that.cpath.length() >= this.cpath.length()) &&
				    that.cpath.startsWith(this.cpath);
			}  else {
			    return ((that.cpath.length() > this.cpath.length()) &&
			        that.cpath.startsWith(this.cpath));
			}
		    } else {
			if (that.directory) {
			    // if the permission passed in is a directory
			    // specification, make sure that a non-recursive
			    // permission (i.e., this object) can't imply a recursive
			    // permission.
			    if (that.recursive)
				return false;
			    else
				return (this.cpath.equals(that.cpath));
			} else {
			    int last = that.cpath.lastIndexOf(File.separatorChar);
			    if (last == -1)
				return false;
			    else {
				// this.cpath.equals(that.cpath.substring(0, last+1));
				// Use regionMatches to avoid creating new string

				return (this.cpath.length() == (last + 1)) &&
				    this.cpath.regionMatches(0, that.cpath, 0, last+1);
			    }
			}
		    }
		} else {
		    return (this.cpath.equals(that.cpath));
		}
	    }

	    /**
	     * Checks two FilePermission objects for equality. Checks that <i>obj</i> is
	     * a FilePermission, and has the same pathname and actions as this object.
	     * <P>
	     * @param obj the object we are testing for equality with this object.
	     * @return true if obj is a FilePermission, and has the same pathname and
	     * actions as this FilePermission object.
	     */
	    public boolean equals(Object obj) {
		if (obj == this)
		    return true;

		if (! (obj instanceof FilePermission))
		    return false;

		FilePermission that = (FilePermission) obj;

		return (this.mask == that.mask) &&
		    this.cpath.equals(that.cpath) &&
		    (this.directory == that.directory) &&
		    (this.recursive == that.recursive);
	    }

	    /**
	     * Returns the hash code value for this object.
	     *
	     * @return a hash code value for this object.
	     */

	    public int hashCode() {
		return this.cpath.hashCode();
	    }

	    /**
	     * Converts an actions String to an actions mask.
	     *
	     * @param action the action string.
	     * @return the actions mask.
	     */
	    private static int getMask(String actions) {

		int mask = NONE;

		// Null action valid?
		if (actions == null) {
		    return mask;
		}
		// Check against use of constants (used heavily within the JDK)
		if (actions == SecurityConstants.FILE_READ_ACTION) {
		    return READ;
		} else if (actions == SecurityConstants.FILE_WRITE_ACTION) {
		    return WRITE;
		} else if (actions == SecurityConstants.FILE_EXECUTE_ACTION) {
		    return EXECUTE;
		} else if (actions == SecurityConstants.FILE_DELETE_ACTION) {
		    return DELETE;
		}

		char[] a = actions.toCharArray();

		int i = a.length - 1;
		if (i < 0)
		    return mask;

		while (i != -1) {
		    char c;

		    // skip whitespace
		    while ((i!=-1) && ((c = a[i]) == ' ' ||
				       c == '\r' ||
				       c == '\n' ||
				       c == '\f' ||
				       c == '\t'))
			i--;

		    // check for the known strings
		    int matchlen;

		    if (i >= 3 && (a[i-3] == 'r' || a[i-3] == 'R') &&
				  (a[i-2] == 'e' || a[i-2] == 'E') &&
				  (a[i-1] == 'a' || a[i-1] == 'A') &&
				  (a[i] == 'd' || a[i] == 'D'))
		    {
			matchlen = 4;
			mask |= READ;

		    } else if (i >= 4 && (a[i-4] == 'w' || a[i-4] == 'W') &&
					 (a[i-3] == 'r' || a[i-3] == 'R') &&
					 (a[i-2] == 'i' || a[i-2] == 'I') &&
					 (a[i-1] == 't' || a[i-1] == 'T') &&
					 (a[i] == 'e' || a[i] == 'E'))
		    {
			matchlen = 5;
			mask |= WRITE;

		    } else if (i >= 6 && (a[i-6] == 'e' || a[i-6] == 'E') &&
			                 (a[i-5] == 'x' || a[i-5] == 'X') &&
					 (a[i-4] == 'e' || a[i-4] == 'E') &&
					 (a[i-3] == 'c' || a[i-3] == 'C') &&
					 (a[i-2] == 'u' || a[i-2] == 'U') &&
					 (a[i-1] == 't' || a[i-1] == 'T') &&
					 (a[i] == 'e' || a[i] == 'E'))
		    {
			matchlen = 7;
			mask |= EXECUTE;

		    } else if (i >= 5 && (a[i-5] == 'd' || a[i-5] == 'D') &&
					 (a[i-4] == 'e' || a[i-4] == 'E') &&
					 (a[i-3] == 'l' || a[i-3] == 'L') &&
					 (a[i-2] == 'e' || a[i-2] == 'E') &&
					 (a[i-1] == 't' || a[i-1] == 'T') &&
					 (a[i] == 'e' || a[i] == 'E'))
		    {
			matchlen = 6;
			mask |= DELETE;

		    } else {
			// parse error
			throw new IllegalArgumentException(
				"invalid permission: " + actions);
		    }

		    // make sure we didn't just match the tail of a word
		    // like "ackbarfaccept".  Also, skip to the comma.
		    boolean seencomma = false;
		    while (i >= matchlen && !seencomma) {
			switch(a[i-matchlen]) {
			case ',':
			    seencomma = true;
			    /*FALLTHROUGH*/
			case ' ': case '\r': case '\n':
			case '\f': case '\t':
			    break;
			default:
			    throw new IllegalArgumentException(
				    "invalid permission: " + actions);
			}
			i--;
		    }

		    // point i at the location of the comma minus one (or -1).
		    i -= matchlen;
		}

		return mask;
	    }

	    /**
	     * Return the current action mask. Used by the FilePermissionCollection.
	     *
	     * @return the actions mask.
	     */

	    int getMask() {
		return mask;
	    }

	    /**
	     * Return the canonical string representation of the actions.
	     * Always returns present actions in the following order:
	     * read, write, execute, delete.
	     *
	     * @return the canonical string representation of the actions.
	     */
	    private static String getActions(int mask)
	    {
		StringBuffer sb = new StringBuffer();
	        boolean comma = false;

		if ((mask & READ) == READ) {
		    comma = true;
		    sb.append("read");
		}

		if ((mask & WRITE) == WRITE) {
		    if (comma) sb.append(',');
	    	    else comma = true;
		    sb.append("write");
		}

		if ((mask & EXECUTE) == EXECUTE) {
		    if (comma) sb.append(',');
	    	    else comma = true;
		    sb.append("execute");
		}

		if ((mask & DELETE) == DELETE) {
		    if (comma) sb.append(',');
	    	    else comma = true;
		    sb.append("delete");
		}

		return sb.toString();
	    }

	    /**
	     * Returns the "canonical string representation" of the actions.
	     * That is, this method always returns present actions in the following order:
	     * read, write, execute, delete. For example, if this FilePermission object
	     * allows both write and read actions, a call to <code>getActions</code>
	     * will return the string "read,write".
	     *
	     * @return the canonical string representation of the actions.
	     */
	    public String getActions()
	    {
		if (actions == null)
		    actions = getActions(this.mask);

		return actions;
	    }


	    /**
	     * Returns a new PermissionCollection object for storing FilePermission
	     * objects.
	     * <p>
	     * FilePermission objects must be stored in a manner that allows them
	     * to be inserted into the collection in any order, but that also enables the
	     * PermissionCollection <code>implies</code>
	     * method to be implemented in an efficient (and consistent) manner.
	     *
	     * <p>For example, if you have two FilePermissions:
	     * <OL>
	     * <LI>  <code>"/tmp/-", "read"</code>
	     * <LI>  <code>"/tmp/scratch/foo", "write"</code>
	     * </OL>
	     *
	     * <p>and you are calling the <code>implies</code> method with the FilePermission:
	     *
	     * <pre>
	     *   "/tmp/scratch/foo", "read,write",
	     * </pre>
	     *
	     * then the <code>implies</code> function must
	     * take into account both the "/tmp/-" and "/tmp/scratch/foo"
	     * permissions, so the effective permission is "read,write",
	     * and <code>implies</code> returns true. The "implies" semantics for
	     * FilePermissions are handled properly by the PermissionCollection object
	     * returned by this <code>newPermissionCollection</code> method.
	     *
	     * @return a new PermissionCollection object suitable for storing
	     * FilePermissions.
	     */

	    public PermissionCollection newPermissionCollection() {
		return new FilePermissionCollection();
	    }

	    /**
	     * WriteObject is called to save the state of the FilePermission
	     * to a stream. The actions are serialized, and the superclass
	     * takes care of the name.
	     */
	    private void writeObject(ObjectOutputStream s)
	        throws IOException
	    {
		// Write out the actions. The superclass takes care of the name
		// call getActions to make sure actions field is initialized
		if (actions == null)
		    getActions();
		s.defaultWriteObject();
	    }

	    /**
	     * readObject is called to restore the state of the FilePermission from
	     * a stream.
	     */
	    private void readObject(ObjectInputStream s)
	         throws IOException, ClassNotFoundException
	    {
		// Read in the actions, then restore everything else by calling init.
		s.defaultReadObject();
		init(getMask(actions));
	    }
	}

	/**
	 * A FilePermissionCollection stores a set of FilePermission permissions.
	 * FilePermission objects
	 * must be stored in a manner that allows them to be inserted in any
	 * order, but enable the implies function to evaluate the implies
	 * method.
	 * For example, if you have two FilePermissions:
	 * <OL>
	 * <LI> "/tmp/-", "read"
	 * <LI> "/tmp/scratch/foo", "write"
	 * </OL>
	 * And you are calling the implies function with the FilePermission:
	 * "/tmp/scratch/foo", "read,write", then the implies function must
	 * take into account both the /tmp/- and /tmp/scratch/foo
	 * permissions, so the effective permission is "read,write".
	 *
	 * @see java.security.Permission
	 * @see java.security.Permissions
	 * @see java.security.PermissionCollection
	 *
	 * @version 1.73 01/23/03
	 *
	 * @author Marianne Mueller
	 * @author Roland Schemers
	 *
	 * @serial include
	 *
	 */

	final class FilePermissionCollection extends PermissionCollection
	implements Serializable {

	    // Not serialized; see serialization section at end of class
	    private transient List perms;

	    /**
	     * Create an empty FilePermissions object.
	     *
	     */

	    public FilePermissionCollection() {
		perms = new ArrayList();
	    }

	    /**
	     * Adds a permission to the FilePermissions. The key for the hash is
	     * permission.path.
	     *
	     * @param permission the Permission object to add.
	     *
	     * @exception IllegalArgumentException - if the permission is not a
	     *                                       FilePermission
	     *
	     * @exception SecurityException - if this FilePermissionCollection object
	     *                                has been marked readonly
	     */

	    public void add(Permission permission)
	    {
		if (! (permission instanceof FilePermission))
		    throw new IllegalArgumentException("invalid permission: "+
						       permission);
		if (isReadOnly())
		    throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");

	        // No need to synchronize because all adds are done sequentially
		// before any implies() calls
		perms.add(permission);
	    }

	    /**
	     * Check and see if this set of permissions implies the permissions
	     * expressed in "permission".
	     *
	     * @param p the Permission object to compare
	     *
	     * @return true if "permission" is a proper subset of a permission in
	     * the set, false if not.
	     */

	    public boolean implies(Permission permission)
	    {
		if (! (permission instanceof FilePermission))
	   		return false;

		FilePermission fp = (FilePermission) permission;

		int desired = fp.getMask();
		int effective = 0;
		int needed = desired;

		int len = perms.size();
		for (int i = 0; i < len; i++) {
	    	    FilePermission x = (FilePermission) perms.get(i);
		    if (((needed & x.getMask()) != 0) && x.impliesIgnoreMask(fp)) {
			effective |=  x.getMask();
			if ((effective & desired) == desired)
			    return true;
			needed = (desired ^ effective);
		    }
		}
		return false;
	    }

	    /**
	     * Returns an enumeration of all the FilePermission objects in the
	     * container.
	     *
	     * @return an enumeration of all the FilePermission objects.
	     */

	    public Enumeration elements() {
	        // Convert Iterator into Enumeration
		return Collections.enumeration(perms);
	    }

	    private static final long serialVersionUID = 2202956749081564585L;

	    // Need to maintain serialization interoperability with earlier releases,
	    // which had the serializable field:
	    //    private Vector permissions;

	    /**
	     * @serialField permissions java.util.Vector
	     *     A list of FilePermission objects.
	     */
	    private static final ObjectStreamField[] serialPersistentFields = {
	        new ObjectStreamField("permissions", Vector.class),
	    };

	    /**
	     * @serialData "permissions" field (a Vector containing the FilePermissions).
	     */
	    /*
	     * Writes the contents of the perms field out as a Vector for
	     * serialization compatibility with earlier releases.
	     */
	    private void writeObject(ObjectOutputStream out) throws IOException {
		// Don't call out.defaultWriteObject()

		// Write out Vector
		Vector permissions = new Vector(perms.size());
		permissions.addAll(perms);

	        ObjectOutputStream.PutField pfields = out.putFields();
	        pfields.put("permissions", permissions);
	        out.writeFields();
	    }

	    /*
	     * Reads in a Vector of FilePermissions and saves them in the perms field.
	     */
	    private void readObject(ObjectInputStream in) throws IOException,
	    ClassNotFoundException {
		// Don't call defaultReadObject()

		// Read in serialized fields
		ObjectInputStream.GetField gfields = in.readFields();

		// Get the one we want
		Vector permissions = (Vector)gfields.get("permissions", null);
		perms = new ArrayList(permissions.size());
		perms.addAll(permissions);
	    }
	}