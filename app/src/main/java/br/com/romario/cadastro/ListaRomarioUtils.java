package br.com.romario.cadastro;

import com.google.firebase.database.FirebaseDatabase;

public class ListaRomarioUtils {
private static FirebaseDatabase mDatabase;

public static FirebaseDatabase getDatabase() {
   if (mDatabase == null) {
      mDatabase = FirebaseDatabase.getInstance();
      mDatabase.setPersistenceEnabled(true);
   }
return mDatabase;
}

}