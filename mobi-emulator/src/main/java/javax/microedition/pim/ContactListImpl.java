package javax.microedition.pim;

// Referenced classes of package javax.microedition.pim:
// AbstractPIMList, ContactImpl
class ContactListImpl extends AbstractPIMList implements ContactList {

  ContactListImpl(final String name, final int mode) throws PIMException {
    super(1, name, mode);
  }

  @Override
  public Contact createContact() {
    return new ContactImpl(this);
  }

  @Override
  public Contact importContact(final Contact contact) {
    return new ContactImpl(this, contact);
  }

  @Override
  public void removeContact(final Contact contact) throws PIMException {
    removeItem(contact);
  }
}
