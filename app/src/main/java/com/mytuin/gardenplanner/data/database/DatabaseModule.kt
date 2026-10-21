package com.mytuin.gardenplanner.data.database

import android.content.Context
import com.mytuin.gardenplanner.data.dao.ActivityDao
import com.mytuin.gardenplanner.data.dao.AreaDao
import com.mytuin.gardenplanner.data.dao.CultivarDao
import com.mytuin.gardenplanner.data.dao.ExperimentDao
import com.mytuin.gardenplanner.data.dao.ExperimentLinkDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GardenPreferenceDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceHistoryDao
import com.mytuin.gardenplanner.data.dao.HarvestDao
import com.mytuin.gardenplanner.data.dao.HarvestLossDao
import com.mytuin.gardenplanner.data.dao.InboxItemDao
import com.mytuin.gardenplanner.data.dao.MeasurementDao
import com.mytuin.gardenplanner.data.dao.ObservationDao
import com.mytuin.gardenplanner.data.dao.PlanDao
import com.mytuin.gardenplanner.data.dao.PlanTargetDao
import com.mytuin.gardenplanner.data.dao.PlantAliasDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceHistoryDao
import com.mytuin.gardenplanner.data.dao.PlantMultiValueDao
import com.mytuin.gardenplanner.data.dao.PlantNoteDao
import com.mytuin.gardenplanner.data.dao.PlantNumericFactDao
import com.mytuin.gardenplanner.data.dao.PlantProblemRelationshipDao
import com.mytuin.gardenplanner.data.dao.PlantRelationshipDao
import com.mytuin.gardenplanner.data.dao.PlantSourceDao
import com.mytuin.gardenplanner.data.dao.PlantTemporalFactDao
import com.mytuin.gardenplanner.data.dao.ProblemDao
import com.mytuin.gardenplanner.data.dao.ProblemObservationDao
import com.mytuin.gardenplanner.data.dao.RecommendationDao
import com.mytuin.gardenplanner.data.dao.RecommendationEvidenceDao
import com.mytuin.gardenplanner.data.dao.RecommendationFactorDao
import com.mytuin.gardenplanner.data.dao.RotationGroupDao
import com.mytuin.gardenplanner.data.dao.SoilRecordDao
import com.mytuin.gardenplanner.data.dao.SourceDao
import com.mytuin.gardenplanner.data.dao.SpatialObjectDao
import com.mytuin.gardenplanner.data.dao.UserDecisionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideGardenDatabase(
        @ApplicationContext context: Context,
    ): GardenDatabase = GardenDatabaseFactory.create(context)

    @Provides
    fun provideGardenDao(db: GardenDatabase): GardenDao = db.gardenDao()

    @Provides
    fun provideGardenPreferenceDao(db: GardenDatabase): GardenPreferenceDao = db.gardenPreferenceDao()

    @Provides
    fun provideAreaDao(db: GardenDatabase): AreaDao = db.areaDao()

    @Provides
    fun provideSpatialObjectDao(db: GardenDatabase): SpatialObjectDao = db.spatialObjectDao()

    @Provides
    fun provideGrowingSpaceDao(db: GardenDatabase): GrowingSpaceDao = db.growingSpaceDao()

    @Provides
    fun provideGrowingSpaceHistoryDao(db: GardenDatabase): GrowingSpaceHistoryDao = db.growingSpaceHistoryDao()

    @Provides
    fun providePlantDao(db: GardenDatabase): PlantDao = db.plantDao()

    @Provides
    fun providePlantMultiValueDao(db: GardenDatabase): PlantMultiValueDao = db.plantMultiValueDao()

    @Provides
    fun providePlantNumericFactDao(db: GardenDatabase): PlantNumericFactDao = db.plantNumericFactDao()

    @Provides
    fun providePlantTemporalFactDao(db: GardenDatabase): PlantTemporalFactDao = db.plantTemporalFactDao()

    @Provides
    fun providePlantNoteDao(db: GardenDatabase): PlantNoteDao = db.plantNoteDao()

    @Provides
    fun providePlantRelationshipDao(db: GardenDatabase): PlantRelationshipDao = db.plantRelationshipDao()

    @Provides
    fun providePlantProblemRelationshipDao(db: GardenDatabase): PlantProblemRelationshipDao = db.plantProblemRelationshipDao()

    @Provides
    fun providePlantSourceDao(db: GardenDatabase): PlantSourceDao = db.plantSourceDao()

    @Provides
    fun provideRotationGroupDao(db: GardenDatabase): RotationGroupDao = db.rotationGroupDao()

    @Provides
    fun provideSoilRecordDao(db: GardenDatabase): SoilRecordDao = db.soilRecordDao()

    @Provides
    fun providePlantInstanceDao(db: GardenDatabase): PlantInstanceDao = db.plantInstanceDao()

    @Provides
    fun providePlantInstanceHistoryDao(db: GardenDatabase): PlantInstanceHistoryDao = db.plantInstanceHistoryDao()

    @Provides
    fun providePlantAliasDao(db: GardenDatabase): PlantAliasDao = db.plantAliasDao()

    @Provides
    fun provideCultivarDao(db: GardenDatabase): CultivarDao = db.cultivarDao()

    @Provides
    fun provideActivityDao(db: GardenDatabase): ActivityDao = db.activityDao()

    @Provides
    fun provideObservationDao(db: GardenDatabase): ObservationDao = db.observationDao()

    @Provides
    fun provideMeasurementDao(db: GardenDatabase): MeasurementDao = db.measurementDao()

    @Provides
    fun provideHarvestDao(db: GardenDatabase): HarvestDao = db.harvestDao()

    @Provides
    fun provideHarvestLossDao(db: GardenDatabase): HarvestLossDao = db.harvestLossDao()

    @Provides
    fun provideProblemDao(db: GardenDatabase): ProblemDao = db.problemDao()

    @Provides
    fun provideProblemObservationDao(db: GardenDatabase): ProblemObservationDao = db.problemObservationDao()

    @Provides
    fun providePlanDao(db: GardenDatabase): PlanDao = db.planDao()

    @Provides
    fun providePlanTargetDao(db: GardenDatabase): PlanTargetDao = db.planTargetDao()

    @Provides
    fun provideSourceDao(db: GardenDatabase): SourceDao = db.sourceDao()

    @Provides
    fun provideRecommendationDao(db: GardenDatabase): RecommendationDao = db.recommendationDao()

    @Provides
    fun provideRecommendationFactorDao(db: GardenDatabase): RecommendationFactorDao = db.recommendationFactorDao()

    @Provides
    fun provideRecommendationEvidenceDao(db: GardenDatabase): RecommendationEvidenceDao = db.recommendationEvidenceDao()

    @Provides
    fun provideUserDecisionDao(db: GardenDatabase): UserDecisionDao = db.userDecisionDao()

    @Provides
    fun provideExperimentDao(db: GardenDatabase): ExperimentDao = db.experimentDao()

    @Provides
    fun provideExperimentLinkDao(db: GardenDatabase): ExperimentLinkDao = db.experimentLinkDao()

    @Provides
    fun provideInboxItemDao(db: GardenDatabase): InboxItemDao = db.inboxItemDao()
}
