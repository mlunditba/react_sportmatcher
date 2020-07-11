import React from 'react';

import './Pagination.css';
import {Link, withRouter} from "react-router-dom";

const pagination = (props) => (
    <div styleName='container'>
        <p>Showing {props.pageInitIndex}-{props.pageInitIndex + props.pageItemCount - 1} of {props.totalItemCount} entries</p>
        {props.totalPageCount !== 1 ? <div styleName='pageItems'>
            <Link styleName={'pageItem' + (props.pageNum === 1 ? ' disabled' : '')}
                to={props.match.url + '?pageNum=1'}
                exact
                onClick={() => (props.pageNum !== 1)? props.onFetchMatches(1) : null}>
                    <p>First</p>
            </Link>
            { props.pageNum > 2 ?
                <Link styleName='pageItem'
                    to={props.match.url + '?pageNum=' + (props.pageNum - 2)}
                    exact
                    onClick={() => props.onFetchMatches(props.pageNum - 2)}>
                        <p>{props.pageNum - 2}</p>
                </Link> : null
            }
            { props.pageNum > 1 ?
                <Link styleName='pageItem'
                    to={props.match.url + '?pageNum=' + (props.pageNum - 1)}
                    exact
                    onClick={() => props.onFetchMatches(props.pageNum - 1)}>
                        <p>{props.pageNum - 1}</p>
                </Link> : null
            }
            <Link styleName='pageItem active'
                to={props.match.url + '?pageNum=' + (props.pageNum)}
                exact
                onClick={() => props.onFetchMatches(props.pageNum)}>
                    <p>{props.pageNum}</p>
            </Link>
            { (props.totalPageCount - props.pageNum) >= 1 ?
                <Link styleName='pageItem'
                    to={props.match.url + '?pageNum=' + (props.pageNum + 1)}
                    exact
                    onClick={() => props.onFetchMatches(props.pageNum + 1)}>
                        <p>{props.pageNum + 1}</p>
                </Link> : null
            }
            { (props.totalPageCount - props.pageNum) >= 2 ?
                <Link styleName='pageItem'
                    to={props.match.url + '?pageNum=' + (props.pageNum + 2)}
                    exact
                    onClick={() => props.onFetchMatches(props.pageNum + 2)}>
                        <p>{props.pageNum + 2}</p>
                </Link> : null
            }
            <Link styleName={'pageItem' + (props.pageNum === props.totalPageCount ? ' disabled' : '')}
                to={props.match.url + '?pageNum=' + props.totalPageCount}
                exact
                onClick={() => (props.pageNum !== props.totalPageCount)? props.onFetchMatches(props.totalPageCount) : null}>
                    <p>Last</p>
            </Link>
        </div> : null}
    </div>
);

export default withRouter(pagination);