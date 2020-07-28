import * as actionTypes from '../actions/actionTypes';
import { updateObject } from "../../shared/utility";

const initialState = {
    matches: [],
    pageNum: 1,
    totalMatchCount: 0,
    pageInitIndex: 1,
    totalPageCount: 1,
    matchInfo: null,
    isParticipant: false,
    voteBalance: 0,
    userVote: 0,
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

const fetchMatchStart = (state, action) => {
    return updateObject(state, {loading: true});
};

const fetchMatchSuccess = (state, action) => {
    return updateObject(state, {
        matchInfo: action.matchInfo,
        isParticipant: action.isParticipant,
        voteBalance: action.voteBalance,
        userVote: action.userVote,
        loading: false
    });
};

const fetchMatchFailure = (state, action) => {
    return updateObject(state, {loading: false});
};

const reducer = (state = initialState, action) => {
    switch(action.type) {
        case actionTypes.FETCH_MATCHES_REQUEST: return fetchMatchesStart(state, action);
        case actionTypes.FETCH_MATCHES_SUCCESS: return fetchMatchesSuccess(state, action);
        case actionTypes.FETCH_MATCHES_FAILURE: return fetchMatchesFailure(state, action);
        case actionTypes.FETCH_MATCH_REQUEST: return fetchMatchStart(state, action);
        case actionTypes.FETCH_MATCH_SUCCESS: return fetchMatchSuccess(state, action);
        case actionTypes.FETCH_MATCH_FAILURE: return fetchMatchFailure(state, action);
        default: return state;
    }
}

export default reducer;