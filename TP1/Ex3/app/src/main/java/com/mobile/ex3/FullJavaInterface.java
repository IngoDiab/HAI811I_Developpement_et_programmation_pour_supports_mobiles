package com.mobile.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FullJavaInterface extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout _verticalMain = new LinearLayout(this);
        _verticalMain.setOrientation(LinearLayout.VERTICAL);

        //Name
        TextView _nameLabel = new TextView(this);
        _nameLabel.setText(R.string.nom);
        _verticalMain.addView(_nameLabel);

        EditText _nameEdit = new EditText(this);
        _nameEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        _nameEdit.setHint(R.string.insérez_votre_nom);
        _verticalMain.addView(_nameEdit);
        //

        //Surname
        TextView _surnameLabel = new TextView(this);
        _surnameLabel.setText(R.string.prenom);
        _verticalMain.addView(_surnameLabel);

        EditText _surnameEdit = new EditText(this);
        _surnameEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        _surnameEdit.setHint(R.string.insérez_votre_prénom);
        _verticalMain.addView(_surnameEdit);
        //

        //Age
        TextView _ageLabel = new TextView(this);
        _ageLabel.setText(R.string.age);
        _verticalMain.addView(_ageLabel);

        EditText _ageEdit = new EditText(this);
        _ageEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        _ageEdit.setHint(R.string.insérez_votre_age);
        _verticalMain.addView(_ageEdit);
        //

        //Competence
        TextView _competenceLabel = new TextView(this);
        _competenceLabel.setText(R.string.domaine_de_comptance);
        _verticalMain.addView(_competenceLabel);

        EditText _competenceEdit = new EditText(this);
        _competenceEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        _competenceEdit.setHint(R.string.insérez_votre_domaine_de_compétance);
        _verticalMain.addView(_competenceEdit);
        //

        //Phone
        TextView _phoneLabel = new TextView(this);
        _phoneLabel.setText(R.string.téléphone);
        _verticalMain.addView(_phoneLabel);

        EditText _phoneEdit = new EditText(this);
        _phoneEdit.setInputType(InputType.TYPE_CLASS_PHONE);
        _phoneEdit.setHint(R.string.insérez_votre_numéro_de_téléphone);
        _verticalMain.addView(_phoneEdit);
        //

        //Button
        Button _validateButton = new Button(this);
        _validateButton.setText(R.string.validateButton);
        _validateButton.setGravity(Gravity.CENTER_HORIZONTAL);
        _verticalMain.addView(_validateButton);
        //

        setContentView(_verticalMain);
    }
}