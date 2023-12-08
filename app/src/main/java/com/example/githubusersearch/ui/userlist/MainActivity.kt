package com.example.githubusersearch.ui.userlist


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersearch.R
import com.example.githubusersearch.data.remote.response.ItemsItem
import com.example.githubusersearch.databinding.ActivityMainBinding
import com.example.githubusersearch.databinding.MenuLayoutBinding
import com.example.githubusersearch.ui.ViewModelFactory
import com.example.githubusersearch.ui.favorite.FavoriteActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var factory: ViewModelFactory
    private lateinit var menuBinding: MenuLayoutBinding
    private val mainViewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        setSupportActionBar(binding.toolbar)

        mainViewModel.listUsers.observe(this) {
            setData(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.errorMsg.observe(this) {
            showError(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                mainViewModel.findUser(searchBar.text.toString())
                if (searchBar.text.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.anda_harus_mengisi_kata_kunci_pencarian),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                false
            }
        }
        binding.recyclerViewMain.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )

        mainViewModel.getThemeSettings().observe(this@MainActivity) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        binding.toolbar.inflateMenu(R.menu.option_menu)

        with(binding.toolbar.menu) {
            findItem(R.id.lightmode).actionView?.let {
                menuBinding = MenuLayoutBinding.bind(it)
            }
            //light-dark mode switch
            menuBinding.menuSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    menuBinding.switchImg.setImageResource(R.drawable.baseline_dark_mode_24)
                    mainViewModel.saveThemeSettings(true)
                } else {
                    menuBinding.switchImg.setImageResource(R.drawable.baseline_light_mode_24)
                    mainViewModel.saveThemeSettings(false)
                }
            }
            when (this@MainActivity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> menuBinding.menuSwitch.isChecked = true
                Configuration.UI_MODE_NIGHT_NO -> menuBinding.menuSwitch.isChecked = false
            }
        }

        //to favorite activity
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.favorite -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    true
                }

                else -> false
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setData(list: List<ItemsItem>) {
        userAdapter = UserAdapter()
        userAdapter.submitList(list)
        binding.recyclerViewMain.adapter = userAdapter
    }

    private fun showError(errorMsg: String) {
        Toast.makeText(this, "Error! \n$errorMsg", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.recyclerViewMain.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.recyclerViewMain.visibility = View.VISIBLE
        }
    }
}