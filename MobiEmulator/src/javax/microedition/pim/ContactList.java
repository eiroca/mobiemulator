package javax.microedition.pim;

//Referenced classes of package javax.microedition.pim:
//           PIMList, PIMException, Contact
public interface ContactList extends PIMList {
    public abstract Contact createContact();
    public abstract Contact importContact(Contact contact);
    public abstract void removeContact(Contact contact) throws PIMException;
}
