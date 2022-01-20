<template>
  <v-container class="md-3">
    <v-spacer></v-spacer>
    <v-col>
      <v-row> Calculate playout cost for </v-row>
      <v-row>
        <v-checkbox
          v-model="isFullSchedule"
          label="all programmes"
        ></v-checkbox>
        <v-spacer></v-spacer>
        <v-select
          v-model="selectedPgm"
          :disabled="isFullSchedule"
          :items="programmeId"
        ></v-select>
        <v-spacer></v-spacer>
      </v-row>
      <v-row>
        <v-checkbox
          v-model="isBetweenDates"
          label="betweeen dates"
        ></v-checkbox>
        <v-spacer></v-spacer>
        <v-date-picker
          v-model="dates"
          range
          :disabled="!isBetweenDates"
        ></v-date-picker>
        <v-spacer></v-spacer>
      </v-row>
      <v-row>
        <v-btn
        :disabled="!enableCalc"
        @click="getCost()"
        >Calculate</v-btn>
      </v-row>
      <v-row>
      <v-spacer></v-spacer>
      </v-row>
      <v-row>
        <H1>
          {{ cost }}
        </H1>
        <v-spacer></v-spacer>
      </v-row>
    </v-col>
    <v-spacer></v-spacer>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      selectedPgm: '',
      programme: [],
      dates: [ ],
      isFullSchedule: true,
      isBetweenDates: false,
      cost: 0
    };
  },
  computed: {
    programmeId: function () {
      return this.programme.map((item) => item.id)
    },
    costingUrl: function () {
      return this.baseUrl + this.dateParams
    },
    baseUrl: function () {
      return this.isFullSchedule ? 'http://localhost:3000/itv/playoutcost/schedule' :
        'http://localhost:3000/itv/playoutcost/programme/' + this.selectedPgm 
    },
    dateParams: function () {
      return this.isBetweenDates ? '?from=' + this.dates[0] +'&to=' + this.dates[1] : ''
    },
    validatePgmInputs() {
      return this.isFullSchedule || this.selectedPgm !== ''
    },
    validateDateInputs () {
      return !this.isBetweenDates || (this.dates[0] && this.dates[1])
    },
    enableCalc () {
      return this.validatePgmInputs && this.validateDateInputs
    }
  },
  methods: {
    async getCost () {
      this.cost = await fetch(this.costingUrl).then(
        (res) => res.json()
      )
    }
  },
  async fetch() {
    this.programme = await fetch("http://localhost:3000/itv/programme").then(
      (res) => res.json()
    )
  }

};
</script>
