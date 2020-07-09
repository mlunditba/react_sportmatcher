import React, { Component } from 'react';
import Aos from 'aos';

import './Matches.css';
import FilterBar from "../../components/UI/FilterBar/FilterBar";

class Home extends Component {
    componentDidMount() {
        Aos.init({ duration: 1500, once: true, startEvent: 'load' });
    }

    render () {
        const filterCategories = [
            { label: 'Search matches', type: 'text' },
            { label: 'Sport', type: 'select', options: ['Soccer', 'Tennis', 'Rugby'] },
            { label: 'Date', type: 'datetime' },
            { label: 'Vacancies', type: 'select', options: [1, 2, 3] }
        ];

        return (
            <div styleName='matches' id='matches'>
                <div styleName='main'>
                    <h1 data-aos="fade-up" data-aos-delay='0' styleName='title'>Find nearby events and<br/>start playing now</h1>
                    <p data-aos="fade-up" data-aos-delay='200'>Filter among hundreds of active inscriptions with the most diverse sports.</p>
                </div>
                <div styleName='container'>
                    <FilterBar categories={filterCategories} />
                </div>
            </div>
        );
    }
}

export default Home;