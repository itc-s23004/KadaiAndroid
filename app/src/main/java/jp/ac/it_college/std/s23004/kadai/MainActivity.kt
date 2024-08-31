package jp.ac.it_college.std.s23004.kadai

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import jp.ac.it_college.std.s23004.kadai.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val pokeApiService = RetrofitInstance.api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.compareButton.setOnClickListener {
            val pokemon1Name = binding.pokemon1Name.text.toString()

            // ポケモン1のデータを取得して表示
            fetchPokemonSpecies(pokemon1Name)

            // ポケモン2のデータを取得して表示
        }

        // ViewCompatとエッジツーエッジの設定
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchPokemonSpecies(pokemonName: String) {
        pokeApiService.getPokemonSpecies(pokemonName).enqueue(object : Callback<PokemonSpeciesResponse> {
            override fun onResponse(call: Call<PokemonSpeciesResponse>, response: Response<PokemonSpeciesResponse>) {
                if (response.isSuccessful) {
                    val speciesResponse = response.body()
                    speciesResponse?.let {
                        val japaneseName = it.names.find { name -> name.language.name == "ja-Hrkt" }?.name
                        binding.pokemonNameTextView.text = japaneseName

                        // fetch and compare types here using the `fetchPokemon` method or any other relevant method
                    }
                } else {
                    showToast("Failed to fetch Pokémon species")
                }
            }

            override fun onFailure(call: Call<PokemonSpeciesResponse>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}

object RetrofitInstance {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: PokeApiService = retrofit.create(PokeApiService::class.java)
}

interface PokeApiService {
    @GET("pokemon/{name}")
    fun getPokemon(@Path("name") name: String): Call<PokemonResponse>

    @GET("type/{id}")
    fun getType(@Path("id") id: Int): Call<TypeResponse>

    @GET("pokemon-species/{name}")
    fun getPokemonSpecies(@Path("name") name: String): Call<PokemonSpeciesResponse>
}


