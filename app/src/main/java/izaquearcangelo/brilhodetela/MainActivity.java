package izaquearcangelo.brilhodetela;


import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Classe principal da aplicação
 */
public class MainActivity extends Activity {

    private SeekBar brightbar;
    private int brightness;
    private ContentResolver cResolver;
    private Window window;
    TextView txtPerc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instancia do objeto seekbar utilizado na tela.
        brightbar = (SeekBar) findViewById(R.id.brightbar);

        // Texto que exibe a mensagem e a porcentagem na tela.
        txtPerc = (TextView) findViewById(R.id.txtPercentage);

        // "Provedor de conteúdo" -  obtido do contexto da aplicação
        cResolver = getContentResolver();

        // Pega a janela atual.
        window = getWindow();

        //Define o intervalo do seekbar entre  0 e 255
        brightbar.setMax(255);

        //Define o incremento da barra
        brightbar.setKeyProgressIncrement(1);

        try {
            //Obtem o brilho do Sistema atual.
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            //Lança uma exceção se acontecer um erro genérico.
            Log.e("Error", "Não foi possível obter o brilho do Sistema.");
            e.printStackTrace();
        }

        // Define a luminosidade do sistema de acordo com o progresso da barra.
        brightbar.setProgress(brightness);

        //Register OnSeekBarChangeListener, so it can actually change values
        brightbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Set the system brightness using the brightness variable value
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
                //Get the current window attributes
                WindowManager.LayoutParams layoutpars = window.getAttributes();
                //Set the brightness of this window
                layoutpars.screenBrightness = brightness / (float) 255;
                //Apply attribute changes to this window
                window.setAttributes(layoutpars);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                //Nothing handled here
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Define o mínimo de brilho para o seekbar
                if (progress <= 1) {
                    //Set the brightness to 20
                    brightness = 1;
                } else //brightness is greater than 20
                {
                    //Set brightness variable based on the progress bar
                    brightness = progress;
                }
                //Calculate the brightness percentage
                float perc = (brightness / (float) 255) * 100;
                //Set the brightness percentage
                txtPerc.setText((int) perc + " %");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
