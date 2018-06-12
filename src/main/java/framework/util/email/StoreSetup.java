package framework.util.email;

import javax.mail.NoSuchProviderException;
import javax.mail.Store;

/**
 * Created by ctpl00694 on 29/6/16.
 */
public interface StoreSetup {

    Store getStore(String emailAddress, String password) throws NoSuchProviderException;
}
