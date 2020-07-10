import * as actionTypes from '../actions/actionTypes';
import { updateObject } from "../../shared/utility";

const initialState = {
    matches: [],
    pageNum: 1,
    totalMatchCount: 0,
    pageInitIndex: 1,
    totalPageCount: 1,
    loading: false
};

const fetchMatchesStart = (state, action) => {
    return updateObject(state, {loading: true});
};

const fetchMatchesSuccess = (state, action) => {
    return updateObject(state, {
        matches: action.matches,
        totalMatchCount: action.totalMatchCount,
        pageInitIndex: action.pageInitIndex,
        totalPageCount: action.totalPageCount,
        pageNum: action.pageNum,
        loading: false
    });
};

const fetchMatchesFailure = (state, action) => {
    return updateObject(state, {loading: false});
};

const reducer = (state = initialState, action) => {
    switch(action.type) {
        case actionTypes.FETCH_MATCHES_REQUEST: return fetchMatchesStart(state, action);
        case actionTypes.FETCH_MATCHES_SUCCESS: return fetchMatchesSuccess(state, action);
        case actionTypes.FETCH_MATCHES_FAILURE: return fetchMatchesFailure(state, action);
        default: return state;
    }
}

export default reducer;