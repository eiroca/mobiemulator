package javax.microedition.pim;

//Referenced classes of package javax.microedition.pim:
//           AbstractPIMList, ContactImpl
class ContactListImpl extends AbstractPIMList implements ContactList {
    ContactListImpl(String name, int mode) throws PIMException {
        super(1, name, mode);
    }

    public Contact createContact() {
        return new ContactImpl(this);
    }
    public Contact importContact(Contact contact) {
        return new ContactImpl(this, contact);
    }
    public void removeContact(Contact contact) throws PIMException {
        removeItem(contact);
    }
}
