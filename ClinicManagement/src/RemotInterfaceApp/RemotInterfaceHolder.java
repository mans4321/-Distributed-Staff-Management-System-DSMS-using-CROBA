package RemotInterfaceApp;

/**
* RemotInterfaceApp/RemotInterfaceHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from IDLFile.idl
* Thursday, June 16, 2016 6:51:02 AM EDT
*/

public final class RemotInterfaceHolder implements org.omg.CORBA.portable.Streamable
{
  public RemotInterfaceApp.RemotInterface value = null;

  public RemotInterfaceHolder ()
  {
  }

  public RemotInterfaceHolder (RemotInterfaceApp.RemotInterface initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = RemotInterfaceApp.RemotInterfaceHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    RemotInterfaceApp.RemotInterfaceHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return RemotInterfaceApp.RemotInterfaceHelper.type ();
  }

}