// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: {enabled: true},

  modules: [
      '@unocss/nuxt',
      'nuxt-headlessui'
  ],

  runtimeConfig: {
      public: {
          backendHost: "http://localhost:7777"
      }
  },

  compatibilityDate: '2024-11-12'
})