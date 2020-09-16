package com.cw.childrenabc.ui.single

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cw.childrenabc.Constants
import com.cw.childrenabc.R
import com.cw.childrenabc.ToastUtil
import com.cw.childrenabc.VoiceUtil

class SingleFragment : Fragment() {

    private lateinit var singleViewModel: SingleViewModel

//    private lateinit var textViewCurCharactor: TextView
    private lateinit var radioButtonWrong: RadioButton
    private lateinit var radioButtonRight: RadioButton
    private lateinit var imageViewCurImageView: ImageView

    private lateinit var voiceUtil: VoiceUtil

    private var curIndex: Int = -1

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        singleViewModel =
                ViewModelProviders.of(this).get(SingleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_single, container, false)
//        textViewCurCharactor = root.findViewById(R.id.text_cur_charactor)
//        singleViewModel.text.observe(viewLifecycleOwner, Observer {
//            textViewCurCharactor.text = it
//        })

        voiceUtil = VoiceUtil(context)
        VoiceUtil.enableMode = 1

        radioButtonWrong = root.findViewById(R.id.check_wrong)
        radioButtonWrong.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (-1 == curIndex) {
                    activity?.let { ToastUtil.showShort(it, "你的得了" + 100 * singleViewModel.testRightCount / singleViewModel.totalTestCount()  + "分") }
                    return
                }

                if (singleViewModel.isIndexRecorded(curIndex)) {
                    curIndex = singleViewModel.generateNextIndex()
                    if (-1 == curIndex) {
                        activity?.let { ToastUtil.showShort(it, "你的得分是: " + 100 * singleViewModel.testRightCount / singleViewModel.totalTestCount()) }
                        return
                    }
                    return
                }

                singleViewModel.recordUsedIndex(curIndex)

                singleViewModel.testTotalCount += 1

                activity?.let { ToastUtil.showShort(it, "噢噢，选错了，请加油") }
                voiceUtil.playByMedia(R.raw.error)

                curIndex = singleViewModel.generateNextIndex()
                if (-1 == curIndex) {
                    activity?.let { ToastUtil.showShort(it, "你的得分是: " + 100 * singleViewModel.testRightCount / singleViewModel.totalTestCount()) }
                    return
                }
                imageViewCurImageView.setImageResource(curIndex)
            }

        })
        radioButtonRight = root.findViewById(R.id.check_right)
        radioButtonRight.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                if (-1 == curIndex) {
                    activity?.let { ToastUtil.showShort(it, "你的得了" + 100 * singleViewModel.testRightCount / singleViewModel.totalTestCount()  + "分") }
                    return
                }

                if (singleViewModel.isIndexRecorded(curIndex)) {
                    curIndex = singleViewModel.generateNextIndex()
                    if (-1 == curIndex) {
                        activity?.let { ToastUtil.showShort(it, "你的得了" + 100 * singleViewModel.testRightCount / singleViewModel.totalTestCount()  + "分") }
                        return
                    }
                    return
                }
                singleViewModel.recordUsedIndex(curIndex)

                singleViewModel.testRightCount += 1
                singleViewModel.testTotalCount += 1

                activity?.let { ToastUtil.showShort(it, "哈哈，选对了，你真棒") }
                voiceUtil.playByMedia(R.raw.right)
                curIndex = singleViewModel.generateNextIndex()

                if (-1 == curIndex) {
                    activity?.let { ToastUtil.showShort(it, "你得了" + 100 * singleViewModel.testRightCount / singleViewModel.totalTestCount() + "分") }
                    return
                }
                imageViewCurImageView.setImageResource(curIndex)
            }
        })

        imageViewCurImageView = root.findViewById(R.id.cur_image)
        curIndex = singleViewModel.generateNextIndex()
        imageViewCurImageView.setImageResource(curIndex)

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(Constants.TAG, "onCreate: ")
    }

    override fun onStart() {
        super.onStart()
        Log.d(Constants.TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(Constants.TAG, "onResume: ")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(Constants.TAG, "onAttach: ")
    }

    override fun onDetach() {

        Log.d(Constants.TAG, "onDetach: ")
        super.onDetach()
    }

    override fun onPause() {

        Log.d(Constants.TAG, "onPause: ")
        super.onPause()
    }

    override fun onStop() {

        Log.d(Constants.TAG, "onStop: ")
        super.onStop()
    }

    override fun onDestroyView() {

        Log.d(Constants.TAG, "onDestroyView: ")
        super.onDestroyView()
    }

    override fun onDestroy() {

        Log.d(Constants.TAG, "onDestroy: ")
        super.onDestroy()
    }
}
