package com.example.test1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarItemView
import com.google.android.material.navigation.NavigationBarView
import com.example.test1.databinding.ActivityMainBinding
import com.example.test1.databinding.HomeFragBinding
import com.example.test1.databinding.MyInfoFragBinding
import com.example.test1.databinding.WriteFragBinding

import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var writeFragment: WriteFragment
    private lateinit var chatFragment: ChatFragment
    private lateinit var myInfoFragment: MyInfoFragment
    private lateinit var favoriteFragment: FavoriteFragment


    private lateinit var binding : ActivityMainBinding

    val manager = supportFragmentManager

    private lateinit var auth: FirebaseAuth
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 앱초기 실행시 실행하는 조건문
        if(savedInstanceState == null) {
            // 처음에 프래그먼트 콘테이너에 들어갈때는 add를 통해서 컨테이너에 프래그먼트를 추가해준다
            // 따라서 add함수를 통해서 처음 프래그먼트를 결정
            homeFragment = HomeFragment.newInstance()
            manager.beginTransaction().add(R.id.fragment_container, homeFragment).commit()
            binding.bottomNavi.selectedItemId = R.id.bottom_nav_home
        }

        binding.bottomNavi.setOnItemSelectedListener(onBottomNaviItemSelectedListener)


        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser




        if (currentUser == null) {
            // The user is already signed in, navigate to MainActivity
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish() // finish the current activity to prevent the user from coming back to the SignInActivity using the back button
        }

        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


    }


    private val onBottomNaviItemSelectedListener = NavigationBarView.OnItemSelectedListener{

        when(it.itemId) {
            R.id.bottom_nav_home -> {
                homeFragment = HomeFragment.newInstance()
                manager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit()
            }

            R.id.bottom_nav_write -> {
                writeFragment = WriteFragment.newInstance()
                manager.beginTransaction().replace(R.id.fragment_container, writeFragment).commit()
            }

            R.id.bottom_nav_chat -> {
                chatFragment = ChatFragment.newInstance()
                manager.beginTransaction().replace(R.id.fragment_container, chatFragment).commit()
            }

            R.id.bottom_nav_favorite -> {
                favoriteFragment = FavoriteFragment.newInstance()
                manager.beginTransaction().replace(R.id.fragment_container, favoriteFragment).commit()
            }

            R.id.bottom_nav_my_info -> {
                myInfoFragment = MyInfoFragment.newInstance()
                manager.beginTransaction().replace(R.id.fragment_container, myInfoFragment).commit()
            }



        }
        true
    }



    private fun showInit() {
        val transaction = manager.beginTransaction().add(R.id.fragment_container, homeFragment)
        transaction.commit()
    }

     fun signOutAndStartSignInActivity() {
        mAuth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            // Optional: Update UI or show a message to the user
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun editBasicInfo() {
        val intent = Intent(this@MainActivity, BasicInfoActivity::class.java)
        startActivity(intent)
    }


    /*
       private fun initBottomNav() {
           binding.bottomNavi.itemIconTintList = null

           binding.bottomNavi.setOnItemSelectedListener {
               when(it.itemId) {
                   R.id.bottom_nav_home -> {
                       HomeFragment().changeFragment()
                   }

                   R.id.bottom_nav_write -> {
                       WriteFragment().changeFragment()
                   }

                   R.id.bottom_nav_chat -> {
                       ChatFragment().changeFragment()
                   }

                   R.id.bottom_nav_favorite -> {
                       FavoriteFragment().changeFragment()
                   }

                   R.id.bottom_nav_my_info -> {
                       MyInfoFragment().changeFragment()
                   }

               }

               return@setOnItemSelectedListener true
           }
           binding.bottomNavi.setOnItemSelectedListener {return@setOnItemSelectedListener true} //바텀네비 재클릭시 화면 재생성 방지
       }

       private fun Fragment.changeFragment() {
           manager.beginTransaction().replace(R.id.fragment_container, this).commit()
       }*/
}




/*package com.example.test1



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)



        val textView = findViewById<TextView>(R.id.name)

        val auth = Firebase.auth
        val user = auth.currentUser

        if (user != null) {
            val userName = user.displayName
            textView.text = "Welcome, " + userName
        } else {
            // Handle the case where the user is not signed in
        }




// Inside onCreate() method
        val sign_out_button = findViewById<Button>(R.id.logout_button)
        sign_out_button.setOnClickListener {
            signOutAndStartSignInActivity()
        }




    }


    private fun signOutAndStartSignInActivity() {
        mAuth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            // Optional: Update UI or show a message to the user
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
*/
