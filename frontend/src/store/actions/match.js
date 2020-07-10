import axios from 'axios';
import * as actionTypes from './actionTypes';

export const fetchMatchesStart = () => {
    return {
        type: actionTypes.FETCH_MATCHES_REQUEST
    }
}

export const fetchMatchesSuccess = (matches, totalMatchCount, pageInitIndex, totalPageCount, pageNum) => {
    return {
        type: actionTypes.FETCH_MATCHES_SUCCESS,
        matches: matches,
        totalMatchCount: totalMatchCount,
        pageInitIndex: pageInitIndex,
        totalPageCount: totalPageCount,
        pageNum: pageNum
    }
}

export const fetchMatchesFailure = (error) => {
    return {
        type: actionTypes.FETCH_MATCHES_FAILURE,
        error: error
    }
}

export const fetchMatches = (pageNum) => {
    return dispatch => {
        dispatch(fetchMatchesStart());
        const queryParams = '?pageNum=' + pageNum;
        axios.get( '/events' + queryParams )
            .then( res => {
                dispatch(fetchMatchesSuccess(res.data.events, res.data.eventCount, res.data.initialPageIndex, res.data.finalPageNum, pageNum));
            } )
            .catch( err => {
                dispatch(fetchMatchesFailure(err));
            } );
    };
};