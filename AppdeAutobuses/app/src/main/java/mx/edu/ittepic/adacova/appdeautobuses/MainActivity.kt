package mx.edu.ittepic.adacova.appdeautobuses

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    
    //    variable
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout:DrawerLayout
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //nuestra drawer laayot
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener{
            it.isChecked=true

            when(it.itemId){
               // R.id.nav_home-> replaceFragment(ConfiguracionFragment())
                R.id.nav_home->replaceFragment(HomeFragment(), it.title.toString())
                R.id.nav_generarreporte-> openActivity()
                    R.id.nav_confi->replaceFragment(ConfiguracionFragment(),it.title.toString())
                R.id.nav_infogeneral->replaceFragment(InfoGeneralFragment(),it.title.toString())
                R.id.nav_viajeencurso->replaceFragment(ViajeEnCursoFragment(),it.title.toString())
           //     R.id.nav_histviajes->replaceFragment(HistorialViajesFragment(),it.title.toString())
                R.id.nav_histviajes->openActivity2()
//                R.id.nav_home->Toast.makeText(applicationContext,"Clicked home", Toast.LENGTH_SHORT).show()
                R.id.nav_generaralerta->openActivity3()
            }
            true
        }
    }

    //vamos a pasar el fragmento
   private fun replaceFragment(fragment: Fragment, title:String){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
       // drawerLayout.closeDrawers()
        setTitle(title)
    }

    private fun openActivity(){
        val intent = Intent(this, GenerarReporte::class.java)
        startActivity(intent)
   }
    private fun openActivity2(){
        val intent = Intent(this, HistorialViajesActivity::class.java)
        startActivity(intent)
    }
    private fun openActivity3(){
        val intent = Intent(this, AlertaActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){

            return true}
            return super.onOptionsItemSelected(item)

    }
    private fun Intent.startActivity(intent: Intent?) {

    }

}


