<template>
    <div class="p-4 flex flex-col gap-y-1">
        <p><NuxtLink to="issuance" class="underline">Issuance</NuxtLink></p>
        <p><NuxtLink to="verification" class="underline">Verification</NuxtLink></p>
        <p>Will use backend at: <code class="text-sm text-gray-600">{{ runtimeConfig }}</code></p>
    </div>

</template>

<script setup lang="ts">
const runtimeConfig = useRuntimeConfig().public.backendHost
</script>
