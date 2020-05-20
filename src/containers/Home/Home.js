import React, { Component } from 'react';
import Aos from 'aos';
import CountUp from 'react-countup';

import './Home.css';
import HomeCard from '../../components/Home/HomeCard/HomeCard';

class Home extends Component {
    componentDidMount() {
        Aos.init({ duration: 1500, once: true, startEvent: 'load' });
    }

    render () {
        return (
            <div styleName='home' id='home'>
                <div styleName='main'>
                        <h1 data-aos="fade-up" data-aos-delay='0' styleName='title'>Sports like you never thought was possible</h1>
                        <p data-aos="fade-up" data-aos-delay='200'>Find people who share your passion. Follow a healthy and active lifestyle.<br/>Playing sports was never this easy.</p>
                </div>
                <div styleName='cards'>
                    <div styleName='counter-container'>
                        <div data-aos="fade-up" styleName='counter'>
                            <CountUp start={0} end={30000} separator=',' suffix='+' />
                            <p>Players</p>
                        </div>
                        <div data-aos="fade-up" data-aos-delay='200' styleName='counter'>
                            <CountUp end={250} delay={0.5} separator=',' suffix='+' />
                            <p>Clubs</p>
                        </div>
                        <div data-aos="fade-up" data-aos-delay='400'styleName='counter'>
                            <CountUp end={700} delay={0.4} separator=',' suffix='+' />
                            <p>Pitches</p>
                        </div>
                        <div data-aos="fade-up" data-aos-delay='600' styleName='counter'>
                            <CountUp end={50} delay={0.6} separator=',' suffix='+' />
                            <p>Tournaments</p>
                        </div>
                    </div>
                    <h2 data-aos="fade-up">Explore every feature SportMatcher has to offer and make the most out of it.</h2>
                    <HomeCard imageUrl='/soccer.jpg' imageAlt='Soccer' imgLeft title="Search among hundreds of available matches." btnTxt='Find matches'>
                        Join any match you want across a variety of different sports in a simple way. Just filter the nearest clubs and events that best suit you and your schedule. Your team is waiting for you!
                    </HomeCard>
                    <HomeCard imageUrl='/tennis.jpg' imageAlt='Tennis' title="Socialize, rate players and make new friends." btnTxt='Find people'>
                        Meet people nearby who share the same interests as you. Chat and invite them to new matches as you expand your list of contacts. Making friends has never been simpler.
                    </HomeCard>
                    <HomeCard imageUrl='/basketball.jpg' imageAlt='Basketball' imgLeft title="Organize events and empower your profile." btnTxt='Create matches'>
                        Become a respected member of our community by hosting events and getting upvoted. Rank up as you become a veteran and people will look forward to playing with you!
                    </HomeCard>
                    <HomeCard imageUrl='/tournament.jpg' imageAlt='Tournament' title="Join a team to take part in our exclusive tournaments." btnTxt='Find tournaments'>
                        Gather your friends and rally at our monthly tournaments for a chance to win awesome prices and achieve glory. Will you be able to lift the champions cup?
                    </HomeCard>
                </div>
            </div>
        );
    }
}

export default Home;