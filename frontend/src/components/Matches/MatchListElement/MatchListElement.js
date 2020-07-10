import React, {Component} from 'react';
import {Link, withRouter} from "react-router-dom";
//import ReactTooltip from "react-tooltip";

import './MatchListElement.css';

import baseballImg from '../../../assets/images/sports/baseball.png';
import basketballImg from '../../../assets/images/sports/basketball.png';
import bowlingImg from '../../../assets/images/sports/bowling.png';
import boxingImg from '../../../assets/images/sports/boxing.png';
import golfImg from '../../../assets/images/sports/golf.png';
import pingPongImg from '../../../assets/images/sports/ping-pong.png';
import rugbyImg from '../../../assets/images/sports/rugby.png';
import soccerImg from '../../../assets/images/sports/soccer.png';
import tennisImg from '../../../assets/images/sports/tennis.png';
import volleyballImg from '../../../assets/images/sports/volleyball.png';

const months = [ // TODO MOVE TO SHARED
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
            case('BASEBALL'):
                img = <img src={baseballImg} alt="baseball" />;
                break;
            case('BASKETBALL'):
                img = <img src={basketballImg} alt="basketball" />;
                break;
            case('BOWLING'):
                img = <img src={bowlingImg} alt="bowling" />;
                break;
            case('BOXING'):
                img = <img src={boxingImg} alt="boxing" />;
                break;
            case('GOLF'):
                img = <img src={golfImg} alt="golf" />;
                break;
            case('PING_PONG'):
                img = <img src={pingPongImg} alt="ping-pong" />;
                break;
            case('RUGBY'):
                img = <img src={rugbyImg} alt="rugby" />;
                break;
            case('SOCCER'):
                img = <img src={soccerImg} alt="soccer" />;
                break;
            case('TENNIS'):
                img = <img src={tennisImg} alt="tennis" />;
                break;
            case('VOLLEYBALL'):
                img = <img src={volleyballImg} alt="volleyball" />;
                break;
            default:
                // TODO ADD DEFAULT IMG
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
            <Link to={this.props.match.url + this.props.match_id} styleName='link'>
                <div styleName='container'>
                    <div styleName='img-container'>{img}</div>
                    <div styleName='outer-label-group'>
                        <div styleName='label-group outer-name-label'>
                            <p styleName='sport-label'>{this.props.sport}</p>
                            <p styleName='name-label'>{this.props.name}</p>
                        </div>
                        <div styleName='label-group location-label'>
                            <p><i className="fa fa-map-marker"></i> {this.props.loc}</p>
                        </div>
                        <div styleName='label-group date-label'>
                            <p>{dateFormat}</p><p styleName='hour-label'>{startHourFormat} - {endHourFormat}</p>
                        </div>
                        <div styleName='label-group vacancies-label' /*data-tip data-for="registerTip"*/>
                            {Array.from(Array(this.props.inscriptionCount), (e,i) => (<div key={i} styleName='vacancies-item red'>|</div>))}
                            {Array.from(Array(this.props.maxParticipants - this.props.inscriptionCount), (e,i) => (<div key={i} styleName='vacancies-item green'>|</div>))}

                        </div>
                        {/*<ReactTooltip id="registerTip" place="top" effect="solid">
                            Tooltip for the register button
                        </ReactTooltip>*/}
                    </div>
                </div>
            </Link>
        );
    }
}

export default withRouter(MatchListElement);