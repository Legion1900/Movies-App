package com.legion1900.moviesapp.view.fragments.mainscreen.adapters

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.paging.PagedListDelegationAdapter
import com.legion1900.moviesapp.data.abs.MoviePager
import com.legion1900.moviesapp.domain.abs.dto.Movie

class BottomNotifierMovieAdapter(
    itemCallback: DiffUtil.ItemCallback<Movie>,
    state: MoviePager.LoadingState,
    manager: AdapterDelegatesManager<List<Movie>>
) : PagedListDelegationAdapter<Movie>(manager, itemCallback) {

    var currentState = state
        set(value) {
            field = value
            notifyItemChanged(itemCount)
        }

    private val isLoading
        get() = currentState == MoviePager.LoadingState.LOADING

    private val isError
        get() = currentState == MoviePager.LoadingState.ERROR

    private val hasSpecialItem: Boolean
        get() {
            return isLoading || isError
        }

    override fun getItemViewType(position: Int): Int {
        val isLoading = currentState == MoviePager.LoadingState.LOADING
        val isError = currentState == MoviePager.LoadingState.ERROR
        val isExtra = position == (currentList?.size ?: 0)

        return when {
            isExtra && isLoading -> VIEW_TYPE_LOADING
            isExtra && isError -> VIEW_TYPE_ERROR
            else -> super.getItemViewType(position)
        }
    }

    override fun getItemCount(): Int {
        val dataSize = super.getItemCount()
        /*
        * super.getItemCount() calls AsyncPagedListDiffer.getItemCount, which returns 0 on
        * dataSource = null;
        * adapter.getItemCount > 0 in turn triggers getItemViewType which throws NullPointer exception
        * on dataSource = null.
        * tl;dr: do not apply +1 when dataSize = 0
        * */
        return if (hasSpecialItem && dataSize != 0) dataSize + 1 else dataSize
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        val viewType = getItemViewType(position)
        if (viewType == VIEW_TYPE_MOVIE) super.onBindViewHolder(holder, position, payloads)
        else {
            getItem(position - 1)
            delegatesManager.onBindViewHolder(
                currentList as List<Movie>,
                position - 1,
                holder,
                payloads
            )
        }
    }
}
