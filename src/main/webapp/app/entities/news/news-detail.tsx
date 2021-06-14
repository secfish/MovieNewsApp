import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './news.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface INewsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const NewsDetail = (props: INewsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { newsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="newsDetailsHeading">
          <Translate contentKey="movieNewsApp.news.detail.title">News</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{newsEntity.id}</dd>
          <dt>
            <span id="headerline">
              <Translate contentKey="movieNewsApp.news.headerline">Headerline</Translate>
            </span>
          </dt>
          <dd>{newsEntity.headerline}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="movieNewsApp.news.url">Url</Translate>
            </span>
          </dt>
          <dd>{newsEntity.url}</dd>
          <dt>
            <span id="pubDate">
              <Translate contentKey="movieNewsApp.news.pubDate">Pub Date</Translate>
            </span>
          </dt>
          <dd>{newsEntity.pubDate ? <TextFormat value={newsEntity.pubDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="movieNewsApp.news.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {newsEntity.image ? (
              <div>
                {newsEntity.imageContentType ? (
                  <a onClick={openFile(newsEntity.imageContentType, newsEntity.image)}>
                    <img src={`data:${newsEntity.imageContentType};base64,${newsEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {newsEntity.imageContentType}, {byteSize(newsEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="movieNewsApp.news.user">User</Translate>
          </dt>
          <dd>{newsEntity.user ? newsEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/news" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/news/${newsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ news }: IRootState) => ({
  newsEntity: news.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(NewsDetail);
