<template>
  <router-view></router-view>
  <UserView />
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import UserView from './views/UserView.vue';

export default defineComponent({
  name: 'App',
  components: {
    UserView
  }
});
</script>

<style>
body {
  margin: 0;
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
}
</style>