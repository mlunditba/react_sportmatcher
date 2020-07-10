import React, { Component } from 'react';
import Aos from 'aos';

import './Matches.css';
import FilterBar from "../../components/UI/FilterBar/FilterBar";
import MatchListElement from "../../components/Matches/MatchListElement/MatchListElement";

const filterCategories = [
    { label: 'Search matches', type: 'text' },
    { label: 'Sport', type: 'select', options: ['Soccer', 'Tennis', 'Rugby'] },
    { label: 'Date', type: 'datetime' },
    { label: 'Vacancies', type: 'select', options: [1, 2, 3] }
];

const matches = [
    {
        createdAt: "2020-07-09T20:22:32.894494Z",
        description: "Evento de prueba. Unanse que la vamos a pasar bien.",
        endsAt: "2020-07-30T18:00:00Z",
        eventid: 1,
        inscriptionCount: 2,
        inscriptionEnd: "2020-07-28T03:00:00Z",
        maxParticipants: 10,
        name: "Evento numero uno",
        owner: { firstname: "Marcos", lastname: "Lund", role: "user", userid: 1, username: "mlund" },
        pitch: {
            club: { clubid: 1, createdAt: "2020-07-07T23:20:43.898507Z", location: "Palermo - Buenos Aires", name: "Tenis Club Argentino"},
            createdAt: "2020-07-09T20:22:27.942536Z",
            name: "Cancha de prueba",
            pitchid: 1,
            sport: "TENNIS"
        },
        startsAt: "2020-07-30T15:00:00Z"
    },
    {
        createdAt: "2020-07-09T20:22:32.894494Z",
        description: "Evento de prueba. Unanse que la vamos a pasar bien.",
        endsAt: "2020-07-30T18:00:00Z",
        eventid: 2,
        inscriptionCount: 2,
        inscriptionEnd: "2020-07-28T03:00:00Z",
        maxParticipants: 10,
        name: "Evento numero uno",
        owner: { firstname: "Marcos", lastname: "Lund", role: "user", userid: 1, username: "mlund" },
        pitch: {
            club: { clubid: 1, createdAt: "2020-07-07T23:20:43.898507Z", location: "Palermo - Buenos Aires", name: "Tenis Club Argentino"},
            createdAt: "2020-07-09T20:22:27.942536Z",
            name: "Cancha de prueba",
            pitchid: 1,
            sport: "SOCCER"
        },
        startsAt: "2020-07-30T15:00:00Z"
    }
];

class Home extends Component {
    componentDidMount() {
        Aos.init({ duration: 1500, once: true, startEvent: 'load' });
    }

    matchHandler = (match_id) => {
        this.props.history.push('/matches/' + match_id);
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
                        {matches.map(m => (
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
                    </div>
                </div>
            </div>
        );
    }
}

export default Home;