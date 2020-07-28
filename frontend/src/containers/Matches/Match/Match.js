import React, {Component} from 'react';
import * as actions from "../../../store/actions";
import {connect} from "react-redux";

class Match extends Component {
    componentDidMount() {
        this.props.onFetchMatch(this.props.location.state.pitch_id, this.props.match.params.id);
    }

    render() {
        //console.log(this.props);
        return (null);
    }
}

const mapStateToProps = state => {
    return {
        matchInfo: state.match.matchInfo,
        isParticipant: state.match.isParticipant,
        voteBalance: state.match.voteBalance,
        userVote: state.match.userVote
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onFetchMatch: (pitch_id, match_id) => dispatch(actions.fetchMatch(pitch_id, match_id))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(Match);