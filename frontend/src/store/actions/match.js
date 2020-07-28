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
                dispatch(fetchMatchesSuccess(res.data.events, res.data.eventCount, res.data.initialPageIndex, res.data.lastPageNum, pageNum));
            } )
            .catch( err => {
                dispatch(fetchMatchesFailure(err));
            } );
    };
};

export const fetchMatchStart = () => {
    return {
        type: actionTypes.FETCH_MATCH_REQUEST
    }
}

export const fetchMatchSuccess = (matchInfo, isParticipant, voteBalance, userVote) => {
    return {
        type: actionTypes.FETCH_MATCH_SUCCESS,
        matchInfo: matchInfo,
        isParticipant: isParticipant,
        voteBalance: voteBalance,
        userVote: userVote
    }
}

export const fetchMatchFailure = (error) => {
    return {
        type: actionTypes.FETCH_MATCH_FAILURE,
        error: error
    }
}

export const fetchMatch = (pitch_id, match_id) => {
    return dispatch => {
        dispatch(fetchMatchStart());
        axios.get( '/pitches/' + pitch_id + '/events/' + match_id)
            .then( res => {
                const matchInfo = {
                    createdAt: res.data.createdAt,
                    description: res.data.description,
                    endsAt: res.data.endsAt,
                    eventId: res.eventid,
                    inscriptionCount: res.data.inscriptionCount,
                    inscriptionEnd: res.data.inscriptionEnd,
                    inscriptionSuccess: res.data.inscriptionSuccess,
                    maxParticipants: res.data.maxParticipants,
                    name: res.data.name,
                    owner: {...res.data.owner},
                    pitch: {...res.data.pitch},
                    startsAt: res.data.startsAt
                };
                dispatch(fetchMatchSuccess(matchInfo, res.data.participant, res.data.voteBalance, res.data.userVote));
            } )
            .catch( err => {
                dispatch(fetchMatchFailure(err));
            } );
    };
};