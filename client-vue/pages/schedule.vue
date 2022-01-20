<template>
  <p v-if="$fetchState.pending">Fetching schedule...</p>
  <p v-else-if="$fetchState.error">An error occurred :(</p>
  <div v-else>
    <h1>Schedule</h1>
      <v-data-table
      :headers="headers"
      :items="schedule"
      :items-per-page="15"
      class="elevation-1"
      ></v-data-table>
      <v-spacer></v-spacer>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        schedule: [],
        headers: [
          {text: 'Programme ID', value: 'programId'},
          {text: 'Start time', value: 'startDateTime'},
          {text: 'End time', value: 'endDateTime'}          
        ]
      }
    },
    async fetch() {
      this.schedule = await fetch(
        'http://localhost:3000/itv/schedule'
      ).then(res => res.json())
    }
  }
</script>