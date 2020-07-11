import React, { Component } from 'react';
import Aos from 'aos';
import { connect } from 'react-redux';

import './Matches.css';
import FilterBar from "../../components/UI/FilterBar/FilterBar";
import MatchListElement from "../../components/Matches/MatchListElement/MatchListElement";
import * as actions from '../../store/actions';
import Pagination from "../../components/UI/Pagination/Pagination";

const filterCategories = [
    { label: 'Search matches', type: 'text' },
    { label: 'Sport', type: 'select', options: ['Soccer', 'Tennis', 'Rugby'] },
    { label: 'Date', type: 'datetime' },
    { label: 'Vacancies', type: 'select', options: [1, 2, 3] }
];

class Home extends Component {
    componentDidMount() {
        this.props.onFetchMatches(1);
        Aos.init({ duration: 1500, once: true, startEvent: 'load' });
    }

    render () {
        return (
            <div styleName='matches' id='matches'>
                <div styleName='main'>
                    <h1 data-aos="fade-up" data-aos-delay='0' styleName='title'>Find nearby events and<br/>start playing now</h1>
                    <p data-aos="fade-up" data-aos-delay='200'>Filter among hundreds of active inscriptions with the most diverse sports.</p>
                </div>
                <div styleName='container'>
                    <FilterBar categories={filterCategories} />
                    <div styleName='matches-list'>
                        {this.props.matches.map(m => (
                            <MatchListElement
                                key={m.eventid}
                                match_id={m.eventid}
                                name={m.name}
                                sport={m.pitch.sport}
                                loc={m.pitch.club.location}
                                startsAt={m.startsAt}
                                endsAt={m.endsAt}
                                maxParticipants={m.maxParticipants}
                                inscriptionCount={m.inscriptionCount} />
                        ))}
                        <Pagination
                            pageInitIndex={this.props.pageInitIndex}
                            pageItemCount={this.props.matches.length}
                            totalItemCount={this.props.totalMatchCount}
                            pageNum={this.props.pageNum}
                            totalPageCount={this.props.totalPageCount}
                            onFetchMatches={this.props.onFetchMatches} />
                    </div>
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        matches: state.match.matches,
        totalMatchCount: state.match.totalMatchCount,
        pageInitIndex: state.match.pageInitIndex,
        totalPageCount: state.match.totalPageCount,
        pageNum: state.match.pageNum,
        loading: state.match.loading
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onFetchMatches: (pageNum) => dispatch(actions.fetchMatches(pageNum))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(Home);