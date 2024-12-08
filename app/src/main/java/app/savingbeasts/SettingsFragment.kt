package app.savingbeasts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.savingbeasts.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    var _binding: FragmentSettingsBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        return view
    }
    override fun onResume() {
        super.onResume()
        // Cambiar el título en la Toolbar
        activity?.title = getString(R.string.settings)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}