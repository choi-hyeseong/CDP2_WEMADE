package com.home.cdp2app.main.setting.order.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.home.cdp2app.R
import com.home.cdp2app.databinding.MainSettingOrderBinding
import com.home.cdp2app.databinding.MainSettingOrderItemBinding
import com.home.cdp2app.main.setting.order.type.HealthCategory
import com.home.cdp2app.main.setting.order.view.viewmodel.DashboardOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Collections

//대시보드 순서 관리하는 액티비티
@AndroidEntryPoint
class DashboardOrderActivity : AppCompatActivity() {

    private val viewModel : DashboardOrderViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = MainSettingOrderBinding.inflate(layoutInflater)
        initView(bind)
        initObserver(bind)
        setContentView(bind.root)
    }

    private fun initView(view : MainSettingOrderBinding) {
        val adapter = DashboardOrderAdapter(LinkedHashSet(), viewModel)
        val touchHelper = ItemTouchHelper(DashboardDragHelper(adapter))
        view.recycler.adapter = adapter
        touchHelper.attachToRecyclerView(view.recycler)

        view.save.setOnClickListener {
            viewModel.save()
        }
    }

    private fun initObserver(view : MainSettingOrderBinding) {
        viewModel.saveLiveData.observe(this) {
            it.getContent()?.let { isSuccess ->
                if (isSuccess) {
                    Toast.makeText(this, R.string.dashboard_order_save_complete, Toast.LENGTH_SHORT).show()
                    finish() //이전 액티비티로 복귀
                }
                else
                    Toast.makeText(this, R.string.dashboard_order_save_failed, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.orderLivedata.observe(this) {
            ((view.recycler.adapter) as DashboardOrderAdapter).update(it.orders)
        }
    }

    class DashboardOrderAdapter(private var categories : LinkedHashSet<HealthCategory>, private val viewModel: DashboardOrderViewModel) : RecyclerView.Adapter<DashboardOrderViewHolder>(), DashboardDragCallback {

        fun update(categories: LinkedHashSet<HealthCategory>) {
            this.categories = categories
            notifyDataSetChanged()
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardOrderViewHolder {
            val inflater = LayoutInflater.from(parent.context) //inflater 흭득
            val bind = MainSettingOrderItemBinding.inflate(inflater, parent, false)
            return DashboardOrderViewHolder(bind)
        }

        override fun getItemCount(): Int {
            return categories.size
        }

        override fun onBindViewHolder(holder: DashboardOrderViewHolder, position: Int) {
            holder.bind(categories.toList()[position])
        }

        override fun onDrag(beforePos: Int, afterPos: Int) {
            // swap 수행
            val swappableCategory = categories.toMutableList().also {
                Collections.swap(it, beforePos, afterPos)
            }
            categories = LinkedHashSet(swappableCategory)
            viewModel.update(categories)
            notifyItemMoved(beforePos, afterPos)
        }

    }

    class DashboardOrderViewHolder(private val view : MainSettingOrderItemBinding) : RecyclerView.ViewHolder(view.root) {

        //bind 수행시 호출
        fun bind(healthCategory: HealthCategory) {
            view.orderText.text = healthCategory.displayName
        }
    }

    // recycler view drag
    class DashboardDragHelper(private val callback: DashboardDragCallback) : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val flag = ItemTouchHelper.UP or ItemTouchHelper.DOWN //위 아래만 움직이게
            return makeMovementFlags(flag, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            callback.onDrag(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        //현재 여기서는 좌우 움직임이 일어나지 않으므로 미구현
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            return
        }
    }

    // 실제 드래그가 발생했을때 수행할 콜백 클래스 - 어댑터가 구현
    fun interface DashboardDragCallback {
        // 이전위치와 변경된 위치 알려주는 param
        fun onDrag(beforePos : Int, afterPos : Int)
    }
}