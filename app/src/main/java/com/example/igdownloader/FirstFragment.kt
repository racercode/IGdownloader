package com.example.igdownloader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.igdownloader.ImageFetcher.download
import com.example.igdownloader.databinding.FragmentFirstBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

@Serializable
data class MyDataClass(val links: List<String>)
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonFirst.setOnClickListener {
            val link = binding.editTextText.text.toString()

            val coroutineScope = CoroutineScope(Dispatchers.IO)

            coroutineScope.launch {
                if(!Python.isStarted()){
                    Python.start(AndroidPlatform(requireContext()));
                }
                val python = Python.getInstance();
                val USERNAME: String? = System.getProperty("USERNAME")
                val PASSWORD: String? = System.getProperty("PASSWORD")

                val pyObject = python.getModule("downloadImage")
                val urlList = pyObject.callAttr("getLink", link, USERNAME, PASSWORD).asList()
                for(url in urlList) {
                    download(url.toString(), requireContext())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}