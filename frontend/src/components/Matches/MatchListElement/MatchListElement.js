import React, {Component} from 'react';

import './MatchListElement.css';

import tennisImg from '../../../assets/images/sports/tennis.png';
import soccerImg from '../../../assets/images/sports/soccer.png';
import {Link} from "react-router-dom";

const months = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December'
]

const days = [
    'Sun',
    'Mon',
    'Tue',
    'Wed',
    'Thu',
    'Fri',
    'Sat'
]

class MatchListElement extends Component {
    render() {
        let img = null;
        switch(this.props.sport) {
            case('TENNIS'):
                img = <img src={tennisImg} alt="tennis" />;
                break;
            case('SOCCER'):
                img = <img src={soccerImg} alt="soccer" />;
                break;
            default: // ADD DEFAULT IMG
                break;
        }

        const startDate = new Date(this.props.startsAt);
        const endDate = new Date(this.props.endsAt);

        const monthName = months[startDate.getMonth()]
        const dayName = days[startDate.getDay()]
        const dateFormat = `${dayName}, ${startDate.getDate()} ${monthName}`;

        const startHour = startDate.getHours();
        const startMinute = startDate.getMinutes();
        const endHour = endDate.getHours();
        const endMinute = endDate.getMinutes();
        const startHourFormat = `${(startHour < 10 ? '0' : '') + startHour}:${(startMinute < 10 ? '0' : '') + startMinute}`;
        const endHourFormat = `${(endHour < 10 ? '0' : '') + endHour}:${(endMinute < 10 ? '0' : '') + endMinute}`;

        return (
            <Link to={'/matches/' + this.props.match_id} styleName='link'>
            <div styleName='container'>

                    <div styleName='img-container'>{img}</div>
                    <div styleName='outer-label-group'>
                        <div styleName='label-group outer-name-label'>
                            <p styleName='sport-label'>{this.props.sport}</p>
                            <p styleName='name-label'>{this.props.name}</p>
                        </div>
                        <div styleName='label-group location-label'>
                            <p><i className="fa fa-map-marker"></i> {this.props.location}</p>
                        </div>
                        <div styleName='label-group date-label'>
                            <p>{dateFormat}<br/><p styleName='hour-label'>{startHourFormat} - {endHourFormat}</p></p>
                        </div>
                        <div styleName='label-group vacancies-label'>
                            {Array.from(Array(this.props.inscriptionCount), () => (<div styleName='vacancies-item red'>|</div>))}
                            {Array.from(Array(this.props.maxParticipants - this.props.inscriptionCount), () => (<div styleName='vacancies-item green'>|</div>))}
                        </div>
                    </div>

            </div>
            </Link>
        );
    }
}

export default MatchListElement;