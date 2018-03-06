package br.com.romario.cadastro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.alexandre.listacompras.R;

import br.com.romario.cadastro.model.Produto;

public class MainListaRomarioActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText edtDescricao, edtQuantidade;


    ListView listV_dados;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Produto> listItem = new ArrayList<>();

    private ArrayAdapter<Produto> arrayAdapterPessoa;

    Produto itemSelecionada;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtQuantidade = (EditText)findViewById(R.id.edtQuantidade);
        edtDescricao = (EditText)findViewById(R.id.editDescricao);
        listV_dados = (ListView) findViewById(R.id.listV_dados);

        inicializarFirebase();
        eventoDatabase();


        listV_dados.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelecionada = (Produto) parent.getItemAtPosition(position);
                edtDescricao.setText(itemSelecionada.getDescricao());
                edtQuantidade.setText(itemSelecionada.getQuantidade().toString());

            }
        });


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Toast.makeText(getApplicationContext(), "Bem vindo de volta " + user.getEmail() + "!", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, LoginListaRomarioActivity.class);
            startActivity(intent);
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novoItem();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void novoItem() {
        Produto p = new Produto();
        p.setuIdProduto(UUID.randomUUID().toString());
        p.setDescricao(edtDescricao.getText().toString());
        p.setQuantidade(Double.valueOf(edtQuantidade.getText().toString()));
        databaseReference.child("listaRomario").child(p.getuIdProduto()).setValue(p);
        limparCampos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            mAuth.signOut();
            startActivity(new Intent(this, LoginListaRomarioActivity.class));
            finish();
            return true;
        } else if (id == R.id.action_sobre){
            startActivity(new Intent(this, AboutListaRomarioActivity.class));
        }
//        else if(id == R.id.menu_novo){
//            Item p = new Item();
//            p.setuId(UUID.randomUUID().toString());
//            p.setDescricao(edtDescricao.getText().toString());
//            p.setQuantidade(Double.valueOf(edtQuantidade.getText().toString()));
//            databaseReference.child("FolhaPonto").child(p.getuId()).setValue(p);
//            limparCampos();
//        }
        else if(id == R.id.menu_atualiza){
            Produto p = new Produto();
            p.setuIdProduto(itemSelecionada.getuIdProduto());
            p.setDescricao(edtDescricao.getText().toString().trim());
            p.setQuantidade(Double.valueOf(edtQuantidade.getText().toString()));
            databaseReference.child("listaRomario").child(p.getuIdProduto()).setValue(p);
            limparCampos();
        } else if (id == R.id.menu_deleta){
            Produto p = new Produto();
            p.setuIdProduto(itemSelecionada.getuIdProduto());
            databaseReference.child("listaRomario").child(p.getuIdProduto()).removeValue();
            limparCampos();
        }
        //TODO return true
        return super.onOptionsItemSelected(item);
    }

    private void eventoDatabase() {
        databaseReference.child("listaRomario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listItem.clear();
                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    Produto p = objSnapshot.getValue(Produto.class);
                    listItem.add(p);
                }
                arrayAdapterPessoa = new ArrayAdapter<Produto>(MainListaRomarioActivity.this, android.R.layout.simple_list_item_1, listItem);

                listV_dados.setAdapter(arrayAdapterPessoa);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void  inicializarFirebase(){
        FirebaseApp.initializeApp(MainListaRomarioActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase = ListaRomarioUtils.getDatabase();
        databaseReference = firebaseDatabase.getReference();
    }

    private void limparCampos() {
        edtQuantidade.setText("");
        edtDescricao.setText("");
    }



}
