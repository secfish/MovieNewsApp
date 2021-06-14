import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IMovie } from 'app/shared/model/movie.model';
import { getEntities as getMovies } from 'app/entities/movie/movie.reducer';
import { getEntity, updateEntity, createEntity, reset } from './twitter.reducer';
import { ITwitter } from 'app/shared/model/twitter.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITwitterUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TwitterUpdate = (props: ITwitterUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { twitterEntity, movies, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/twitter');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getMovies();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.pubDate = convertDateTimeToServer(values.pubDate);

    if (errors.length === 0) {
      const entity = {
        ...twitterEntity,
        ...values,
        movie: movies.find(it => it.id.toString() === values.movieId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="movieNewsApp.twitter.home.createOrEditLabel" data-cy="TwitterCreateUpdateHeading">
            <Translate contentKey="movieNewsApp.twitter.home.createOrEditLabel">Create or edit a Twitter</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : twitterEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="twitter-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="twitter-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="contentLabel" for="twitter-content">
                  <Translate contentKey="movieNewsApp.twitter.content">Content</Translate>
                </Label>
                <AvField
                  id="twitter-content"
                  data-cy="content"
                  type="text"
                  name="content"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="pubDateLabel" for="twitter-pubDate">
                  <Translate contentKey="movieNewsApp.twitter.pubDate">Pub Date</Translate>
                </Label>
                <AvInput
                  id="twitter-pubDate"
                  data-cy="pubDate"
                  type="datetime-local"
                  className="form-control"
                  name="pubDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.twitterEntity.pubDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="publisherLabel" for="twitter-publisher">
                  <Translate contentKey="movieNewsApp.twitter.publisher">Publisher</Translate>
                </Label>
                <AvField id="twitter-publisher" data-cy="publisher" type="text" name="publisher" />
              </AvGroup>
              <AvGroup>
                <Label for="twitter-movie">
                  <Translate contentKey="movieNewsApp.twitter.movie">Movie</Translate>
                </Label>
                <AvInput id="twitter-movie" data-cy="movie" type="select" className="form-control" name="movieId">
                  <option value="" key="0" />
                  {movies
                    ? movies.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/twitter" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  movies: storeState.movie.entities,
  twitterEntity: storeState.twitter.entity,
  loading: storeState.twitter.loading,
  updating: storeState.twitter.updating,
  updateSuccess: storeState.twitter.updateSuccess,
});

const mapDispatchToProps = {
  getMovies,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TwitterUpdate);
